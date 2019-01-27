package terrains

import models.RawModel
import renderEngine.Loader
import textures.ModelTexture



class Terrain {

    var x: Float
    var z: Float
    var model: RawModel
    var texture: ModelTexture

    constructor(gridX: Float,
                gridZ: Float,
                loader: Loader,
                texture: ModelTexture) {
        this.x = gridX * SIZE
        this.z = gridZ * SIZE
        this.texture = texture
        this.model = generateTerrain(loader)
    }

    companion object {
        private const val SIZE = 800
        private const val VERTEX_COUNT = 128
    }


    private fun generateTerrain(loader: Loader): RawModel {
        val count = VERTEX_COUNT * VERTEX_COUNT
        val vertices = FloatArray(count * 3)
        val normals = FloatArray(count * 3)
        val textureCoords = FloatArray(count * 2)
        val indices = IntArray(6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1))
        var vertexPointer = 0
        for (i in 0 until VERTEX_COUNT) {
            for (j in 0 until VERTEX_COUNT) {
                vertices[vertexPointer * 3] = j.toFloat() / (VERTEX_COUNT.toFloat() - 1) * SIZE
                vertices[vertexPointer * 3 + 1] = 0f
                vertices[vertexPointer * 3 + 2] = i.toFloat() / (VERTEX_COUNT.toFloat() - 1) * SIZE
                normals[vertexPointer * 3] = 0f
                normals[vertexPointer * 3 + 1] = 1f
                normals[vertexPointer * 3 + 2] = 0f
                textureCoords[vertexPointer * 2] = j.toFloat() / (VERTEX_COUNT.toFloat() - 1)
                textureCoords[vertexPointer * 2 + 1] = i.toFloat() / (VERTEX_COUNT.toFloat() - 1)
                vertexPointer++
            }
        }
        var pointer = 0
        for (gz in 0 until VERTEX_COUNT - 1) {
            for (gx in 0 until VERTEX_COUNT - 1) {
                val topLeft = gz * VERTEX_COUNT + gx
                val topRight = topLeft + 1
                val bottomLeft = (gz + 1) * VERTEX_COUNT + gx
                val bottomRight = bottomLeft + 1
                indices[pointer++] = topLeft
                indices[pointer++] = bottomLeft
                indices[pointer++] = topRight
                indices[pointer++] = topRight
                indices[pointer++] = bottomLeft
                indices[pointer++] = bottomRight
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices)
    }

}