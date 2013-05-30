/*
 * Simplex3dEngine - Renderer Module
 * Copyright (C) 2011, Aleksey Nikiforov
 *
 * This file is part of Simplex3dEngine.
 *
 * Simplex3dEngine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Simplex3dEngine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package simplex3d.engine
package graphics.pluggable

import simplex3d.math._
import simplex3d.math.double._
import simplex3d.math.double.functions._
import simplex3d.data._
import simplex3d.data.double._
import simplex3d.engine.util._
import simplex3d.engine.scene._
import simplex3d.engine.scene.api._
import simplex3d.engine.graphics._


import simplex3d.engine.scene.api._ // use this to get access to protected methods on, say, ManagedScene

class PassManager[G <: graphics.GraphicsContext] extends graphics.PassManager[G] {

    val dim = Vec2i(1024,768)
    val defaultFbo = new FrameBuffer(dim)
    defaultFbo.attach( {
                          val t = Texture2d[Vec4](dim)
                          // FIXME: Why are mipmaps & anisotropy
                          // causing later downsampling passes of the
                          // texture to get darker and darker?
                          t.mipMapFilter = MipMapFilter.Disabled
                          //
                          // if mipmap generation is active,
                          // anisotropy must not be 0, otherwise
                          // downsampling the texture will cause the
                          // downsampled image to get black. It will
                          // get black either way, but higher
                          // anisotropy values appear to slow it
                          // down...
                          // t.anisotropyLevel = 0
                          t
                      }, FBOAttachmentPoint.ColorAP)
    defaultFbo.attach( DepthTexture2d[RDouble](dim), FBOAttachmentPoint.DepthAP)
    //val passes = List[Pass](new Pass(DefaultFrameBuffer.instance))
    var passes = List[Pass](new Pass(defaultFbo))

    private var renderArray = new SortBuffer[AbstractMesh]()

    /** Return the techniques (as Option[Technique]) as specified inside the individual passes.
      *
      */
    def techniques = passes map(_.technique)

    /** Render the given scene, using the renderManager.
      * This will render the scene using all of the configured passes.
      */
    def render(renderManager: RenderManager, time: TimeStamp, scene: ManagedScene[G]) {
        // first render the scene as normal
        for(pass <- passes) {
            renderArray.clear()

            renderManager.renderContext.bindFrameBuffer (pass.frameBuffer)
            pass.preRender(time)
            if(pass.clearBuffer)
                renderManager.renderContext.clearFrameBuffer()


            var camera = scene.camera

            if (pass.scene == None) {
                // use the default scene
                scene.buildRenderArray(pass, time, renderArray)
            } else if (pass.scene != None) {
                // use the sg from pass!
                pass.scene.get.buildRenderArray (pass, time, renderArray)
                camera = pass.scene.get.camera
            } else {
                // do nothing
            }
            // override camera if explicitly specified
            pass.camera map { camera = _ }

            // filter out meshes
            pass.meshFilter map { meshFilter =>
                val newRenderArray = new SortBuffer[AbstractMesh]()
                for(mesh <- renderArray) {
                    if(meshFilter(mesh))
                        newRenderArray += mesh
                }
                renderArray = newRenderArray
            }

            // sorts the renderArray (that contains instances of AbstractMesh)
            renderManager.sortRenderArray(pass, renderArray)
            // renders the meshes inside the renderArray
            renderManager.render(time, camera, renderArray)

            renderManager.renderContext.unbindFrameBuffer (pass.frameBuffer)

            pass.postRender(time)
        }
        // TODO: Figure out how to get the texture from the previous pass
        // into the next pass. Maybe the Pass "trigger"?

        // We need one default pass, that draws a quad, without any
        // fbo bound. The quad however, should have the texture of the
        // prior pass bound.
        val fse = new FullscreenEffect ("FinalPassQuad") {
            protected val colorTexture = Value(new TextureBinding[Texture2d[_ <: simplex3d.data.Accessor]]);
            val passToShow = finalPass match {
                case x if x < 0 => {
                    passes(max(0,passes.length + finalPass))
                }
                case other => {
                    passes(min(passes.length - 1, other))
                }
            }
            passToShow.frameBuffer.depthAttachment map { colorTexture.update := _ }
            passToShow.frameBuffer.colorAttachment map { colorTexture.update := _ }
            //colorTexture.update := defaultFbo.colorAttachment.get.asInstanceOf [DepthTexture2d [RDouble]]

            override val vertexShader = new Shader(Shader.Vertex,"""
    attribute vec3 vertices;
    varying vec3 vVertices;
    void main() {
        //vVertices = vertices / 2.0 - vec3(0.5,0.5,0.0);
        vVertices = vertices;
        gl_Position = vec4(vVertices, 1.0);
    }
""")

            override val fragmentShader = """
    uniform sampler2D colorTexture;
    varying vec3 vVertices;

    void main() {
        //gl_FragColor = vec4(1.0,0.0,0.0,1.0);
        vec2 coord = (vVertices.xy + 1.0) / 2.0;
        vec4 color = vec4(1.0);
        if(coord.x <= 0.005 || coord.y <= 0.005 || coord.x >= 0.995 || coord.y >= 0.995) {
            color = vec4(1.0,1.0,0.0,1.0);
        } else {
            color = texture2D(colorTexture, coord);
        }
        gl_FragColor = color;
    }

"""

            override def prepMesh (mesh : BaseMesh) {
                //mesh.glDebugging.resetGlState = true
            }
        }

        renderManager.renderContext.clearFrameBuffer()
        //renderManager.render(time, scene.camera, renderArray)
        fse.render(renderManager, time)
    }
}
