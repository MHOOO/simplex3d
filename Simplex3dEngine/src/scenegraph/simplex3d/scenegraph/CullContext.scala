/*
 * Simplex3dEngine - SceneGraph Module
 * Copyright (C) 2012, Aleksey Nikiforov
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

import simplex3d.engine._
import simplex3d.engine.util._
import simplex3d.engine.scene._
import simplex3d.engine.transformation._
import simplex3d.engine.graphics._


final class CullContext[T <: TransformationContext, G <: GraphicsContext](
  val renderArray: SortBuffer[SceneElement[T, G]],
  val time: TimeStamp,
  val view: View,
  val batchChildrenThreshold: Int, // applies when parsing scenegraph with multithreading enabled
  val batchDepthThreshold: Int, // applies when parsing scenegraph with multithreading enabled
  val batchArray: SortBuffer[SceneElement[T, G]] // used to accumulate elements
)
