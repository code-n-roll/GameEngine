package renderEngine

import models.RawModel
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.newdawn.slick.opengl.Texture
import org.newdawn.slick.opengl.TextureLoader

import java.io.FileInputStream
import java.io.IOException
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.util.ArrayList

class Loader {

    private val vaos = ArrayList<Int>()
    private val vbos = ArrayList<Int>()
    private val textures = ArrayList<Int>()

    fun loadToVAO(positions: FloatArray,
                  textureCoords: FloatArray,
                  normals: FloatArray,
                  indices: IntArray): RawModel {
        val vaoID = createVAO()
        bindIndicesBuffer(indices)
        storeDataInAttributeList(0, 3, positions)
        storeDataInAttributeList(1, 2, textureCoords)
        storeDataInAttributeList(2, 3, normals)
        unbindVAO()
        return RawModel(vaoID, indices.size)
    }

    fun loadTexture(fileName: String): Int {
        val texture = TextureLoader.getTexture("png",
                FileInputStream("GameEngine/res/$fileName.png"))

        val textureID = texture.textureID
        textures.add(textureID)
        return textureID
    }

    fun cleanUp() {
        for (vao in vaos) {
            GL30.glDeleteVertexArrays(vao)
        }
        for (vbo in vbos) {
            GL15.glDeleteBuffers(vbo)
        }
        for (texture in textures) {
            GL11.glDeleteTextures(texture)
        }
    }

    private fun createVAO(): Int {
        val vaoID = GL30.glGenVertexArrays()
        vaos.add(vaoID)
        GL30.glBindVertexArray(vaoID)
        return vaoID
    }

    private fun storeDataInAttributeList(
            attributeNumber: Int,
            coordinateSize: Int,
            data: FloatArray) {
        val vboID = GL15.glGenBuffers()
        vbos.add(vboID)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID)
        val buffer = storeDataInFloatBuffer(data)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
        GL20.glVertexAttribPointer(attributeNumber,
                coordinateSize,
                GL11.GL_FLOAT,
                false,
                0,
                0)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)

    }

    private fun unbindVAO() {
        GL30.glBindVertexArray(0)
    }

    private fun bindIndicesBuffer(indices: IntArray) {
        val vboID = GL15.glGenBuffers()
        vbos.add(vboID)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID)
        val buffer = storeDataInIntBuffer(indices)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)

    }

    private fun storeDataInIntBuffer(data: IntArray): IntBuffer {
        return BufferUtils.createIntBuffer(data.size).apply {
            put(data)
            flip()
        }
    }

    private fun storeDataInFloatBuffer(data: FloatArray): FloatBuffer {
        return BufferUtils.createFloatBuffer(data.size).apply {
            put(data)
            flip()
        }
    }
}
