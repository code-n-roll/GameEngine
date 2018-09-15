package renderEngine

import entities.Entity
import models.TexturedModel
import org.lwjgl.opengl.*
import org.lwjgl.util.vector.Matrix4f
import shaders.StaticShader
import toolbox.Maths

class Render(shader: StaticShader) {

    companion object {

        private const val FOV = 70f
        private const val NEAR_PLANE = 0.1f
        private const val FAR_PLANE = 1000f
    }

    private lateinit var projectionMatrix: Matrix4f
    private var shader: StaticShader = shader

    init {
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glCullFace(GL11.GL_BACK)
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
        GL11.glClearColor(0.3f, 0f, 0f, 1f)
    }

    fun render(entities: Map<TexturedModel, List<Entity>>) {
        for (model in entities.keys) {
            prepareTexturedModel(model)
            val batch = entities[model] as ArrayList
            for (entity in batch) {
                prepareInstance(entity)
                GL11.glDrawElements(GL11.GL_TRIANGLES,
                        model.rawModel.vertextCount,
                        GL11.GL_UNSIGNED_INT,
                        0)
            }
            unbindTexturedModel()
        }
    }

    private fun prepareTexturedModel(model: TexturedModel) {
        val rawModel = model.rawModel
        GL30.glBindVertexArray(rawModel.vaoID)
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)
        GL20.glEnableVertexAttribArray(2)

        val texture = model.texture
        shader.loadShineVariables(texture.shineDamper, texture.reflectivity)
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.texture.id)
    }

    private fun unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0)
        GL20.glDisableVertexAttribArray(1)
        GL20.glDisableVertexAttribArray(2)
        GL30.glBindVertexArray(0)
    }

    private fun prepareInstance(entity: Entity) {
        val transformationMatrix = Maths.createTransformationMatrix(
                entity.position,
                entity.rotX,
                entity.rotY,
                entity.rotZ,
                entity.scale)
        shader.loadTransformationMatrix(transformationMatrix)
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
}
