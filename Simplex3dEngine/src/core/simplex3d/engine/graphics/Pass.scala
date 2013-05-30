/*
 * Simplex3dEngine - Core Module
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
package graphics

import simplex3d.engine.scene._
import simplex3d.engine.graphics.pluggable._
import simplex3d.engine.transformation._

import simplex3d.math.types._
import simplex3d.math._
import simplex3d.math.double._
import simplex3d.math.double.functions._
import simplex3d.data._
import simplex3d.data.double._
import simplex3d.engine.util._
import simplex3d.engine.graphics.prototype._



// class PassInput () {
//     def receive (value : ???) {

//     }
// }

// class PassOutput () {
//     private val connections: List [PassInput] = List.empty
//     def connectTo (input: PassInput) {
//         connections ++= input
//     }
// }

// TODO: frameBuffer should default to the screen size?
class Pass (
    val frameBuffer: FrameBuffer,
    var camera: Option[AbstractCamera]= None,
    val technique: Option[Technique] = None,
    val triggers: List[String] = Nil // TODO design and implement triggers
        //val inputs: List [PassInput] = List.empty,
        //val outputs: List [PassOutput] = List.empty
) {
    var meshFilter : Option[AbstractMesh => Boolean] = None
    var clearBuffer = true
    var mesh : Option [AbstractMesh] = None
    var scene : Option [ManagedScene[_ <: graphics.GraphicsContext]] = None

    def preRender(time : TimeStamp) {
    }
    def postRender(time : TimeStamp) {
    }
}
