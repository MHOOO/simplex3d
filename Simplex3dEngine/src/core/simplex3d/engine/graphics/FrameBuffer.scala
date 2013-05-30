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

import util._

import java.nio._

import simplex3d.math._

object FBOAttachmentPoint extends Enumeration {
  type FBOAttachmentPoint = Value
  val ColorAP, DepthAP, StencilAP = Value
}

class FrameBuffer(val dimensions: ConstVec2i) extends EngineInfoRef {
  private var attachmentChanges = true

  var colorAttachment : Option [Texture2d [_ <: simplex3d.data.Accessor]] = None
  var depthAttachment : Option [Texture2d [_ <: simplex3d.data.Accessor]] = None
  var stencilAttachment : Option [Texture2d [_ <: simplex3d.data.Accessor]] = None

  // private[engine]
  def hasAttachmentChanges = attachmentChanges
  // private[engine]
  def clearAttachmentChanges() { attachmentChanges = false }

  import FBOAttachmentPoint._
  def attach(tex : Texture2d [_ <: simplex3d.data.Accessor], where : FBOAttachmentPoint) {
    where match {
      case ColorAP => { colorAttachment = Some (tex) }
      case DepthAP => { depthAttachment = Some (tex) }
      case StencilAP => { stencilAttachment = Some (tex) }
    }
    attachmentChanges = true

  }
}

object DefaultFrameBuffer {
  val instance = new FrameBuffer (Vec2i (1024,768))
}
