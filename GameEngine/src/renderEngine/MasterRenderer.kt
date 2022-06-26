package renderEngine

import entities.Camera
import entities.Entity
import entities.Light
import models.TexturedModel
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Matrix4f
import shaders.StaticShader
import shaders.TerrainShader
import terrains.Terrain

class MasterRenderer {

    companion object {

        private const val FOV = 70f
        private const val NEAR_PLANE = 0.1f
        private const val FAR_PLANE = 1000f

        fun enableCulling() {
            GL11.glEnable(GL11.GL_CULL_FACE)
            GL11.glCullFace(GL11.GL_BACK)
        }

        fun disableCulling() {
            GL11.glDisable(GL11.GL_CULL_FACE)
        }
    }

    private lateinit var projectionMatrix: Matrix4f

    val shader = StaticShader()
    val renderer: EntityRenderer

    val terrainRenderer: TerrainRenderer
    val terrainShader: TerrainShader = TerrainShader()

    val entities = HashMap<TexturedModel, ArrayList<Entity>>()
    val terrains = arrayListOf<Terrain>()

    constructor() {
        enableCulling()
        createProjectionMatrix()
        renderer = EntityRenderer(shader, projectionMatrix)
        terrainRenderer = TerrainRenderer(terrainShader, projectionMatrix)
    }

    fun render(sun: Light, camera: Camera) {
        prepare()
        shader.start()
        shader.loadLight(sun)
        shader.loadViewMatrix(camera)
        renderer.render(entities)
        shader.stop()

        terrainShader.start()
        terrainShader.loadLight(sun)
        terrainShader.loadViewMatrix(camera)
        terrainRenderer.render(terrains)
        terrainShader.stop()

        terrains.clear()
        entities.clear()
    }

    fun processTerrain(terrain: Terrain) {
        terrains.add(terrain)
    }

    fun processEntity(entity: Entity) {
        val entityModel = entity.model
        val batch = entities[entityModel]
        if (batch != null) {
            batch.add(entity)
        } else {
            val newBatch = ArrayList<Entity>()
            newBatch.add(entity)
            entities[entityModel] = newBatch
        }
    }

    fun cleanUp() {
        shader.cleanUp()
        terrainShader.cleanUp()
    }

    fun prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        GL11.glClearColor(0.49f, 89f, 0.98f, 1f)
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