/*
 * Simplex3dEngine - GL Module
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

package simplex3d.backend.opengl


/** The ManagedFields class contains an objectType depicting
  * the field type and an id, which in most cases is an OpenGL
  * id of some OpenGL object type (e.g. a texture).
  */
final class ManagedFields(val objectType: Int, var id: Int)

/**
  * The ObjectInfo class stores additional (internal) information
  * for OpenGL objects. In particular this means an OpenGL id inside
  * the managedFields property. Furthermore a ManagedRef is being used
  * to keep a phantom reference to the ObjectInfo. Once the ObjectInfo
  * is being gc'ed, so will the GPU data be cleared.
  * Subclasses can add additional data that should be associated
  * with an OpenGL object (e.g. whether a texture has mipmaps).
  *
  * @param objectType An integer identifying the object type.
  * See the ManagedObjects enum.
  *
  * @see ManagedRef
  * @see ManagedFields
  * @see ManagedObjects
  * @see CompiledInfo
  * @see ProgramInfo
  * @see TextureInfo
  * @see MeshInfo
  */
class ObjectInfo(objectType: Int) {
  /** This property specifies the object type and an internal
    * id (usually the OpenGL id).
    */
  final val managedFields = new ManagedFields(objectType, 0)

  /** This property contains a weak reference to an ObjectInfo
    * and an ObjectInfo queue.
    */
  final var managedRef: ManagedRef = _
}
