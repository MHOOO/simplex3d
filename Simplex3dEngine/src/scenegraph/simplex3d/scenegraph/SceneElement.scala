/*
 * Simplex3dEngine - SceneGraph Module
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

package simplex3d.scenegraph

import scala.collection.mutable.ArrayBuffer
import simplex3d.math.double._
import simplex3d.engine.graphics._
import simplex3d.engine.transformation._


abstract class SceneElement[T <: TransformationContext, G <: GraphicsContext] private[scenegraph] (name: String)(
  implicit transformationContext: T, final val graphicsContext: G
)
extends Spatial[T](name)
{
  
  protected type Graphics = G
  
  private[scenegraph] def onParentChange(
    parent: AbstractNode[T, G], managed: ArrayBuffer[Spatial[T]]
  ) {
    onSpatialParentChange(parent, managed)
  }
}
