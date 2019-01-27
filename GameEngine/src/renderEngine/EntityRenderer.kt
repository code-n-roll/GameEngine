package renderEngine

import entities.Entity
import models.TexturedModel
import org.lwjgl.opengl.*
import org.lwjgl.util.vector.Matrix4f
import shaders.StaticShader
import toolbox.Maths

class EntityRenderer {

    private var shader: StaticShader

    constructor(shader: StaticShader,
                projectionMatrix: Matrix4f) {
        this.shader = shader
        with(shader) {
            start()
            loadProjectionMatrix(projectionMatrix)
            stop()
        }
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
}
