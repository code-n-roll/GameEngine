package renderEngine

import entities.Entity
import models.RawModel
import models.TexturedModel
import org.lwjgl.opengl.*
import org.lwjgl.util.vector.Matrix4f
import shaders.StaticShader
import toolbox.Maths

class Renderer(shader: StaticShader) {

    lateinit var projectionMatrix: Matrix4f

    init {
        createProjectionMatrix()
        with(shader) {
            start()
            loadProjectionMatrix(projectionMatrix)
            stop()
        }
    }

    fun prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        GL11.glClearColor(1f, 0f, 0f, 1f)
    }

    fun render(entity: Entity, shader: StaticShader) {
        val model = entity.model
        val rawModel = model.rawModel
        GL30.glBindVertexArray(rawModel.vaoID)
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)

        val transformationMatrix = Maths.createTransformationMatrix(
                entity.position,
                entity.rotX,
                entity.rotY,
                entity.rotZ,
                entity.scale)
        shader.loadTransformationMatrix(transformationMatrix)

        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.texture.id)
        GL11.glDrawElements(GL11.GL_TRIANGLES,
                rawModel.vertextCount,
                GL11.GL_UNSIGNED_INT,
                0)
        GL20.glDisableVertexAttribArray(0)
        GL20.glDisableVertexAttribArray(1)
        GL30.glBindVertexArray(0)
    }

    private fun createProjectionMatrix() {
        val aspectRatio = Display.getWidth().toFloat() / Display.getHeight().toFloat()
        val y_scale = (1f / Math.tan(Math.toRadians((FOV / 2f).toDouble())) * aspectRatio).toFloat()
        val x_scale = y_scale / aspectRatio
        val frustum_length = FAR_PLANE - NEAR_PLANE

        projectionMatrix = Matrix4f()
        projectionMatrix.m00 = x_scale
        projectionMatrix.m11 = y_scale
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length)
        projectionMatrix.m23 = -1f
        projectionMatrix.m32 = -(2f * NEAR_PLANE * FAR_PLANE / frustum_length)
        projectionMatrix.m33 = 0f
    }

    companion object {

        private const val FOV = 70f
        private const val NEAR_PLANE = 0.1f
        private const val FAR_PLANE = 1000f
    }
}
