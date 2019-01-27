package shaders

import org.lwjgl.util.vector.Matrix4f

import toolbox.Maths
import entities.Camera
import entities.Light

class TerrainShader : ShaderProgram(VERTEX_FILE, FRAGMENT_FILE) {

    private var location_transformationMatrix: Int = 0
    private var location_projectionMatrix: Int = 0
    private var location_viewMatrix: Int = 0
    private var location_lightPosition: Int = 0
    private var location_lightColour: Int = 0
    private var location_shineDamper: Int = 0
    private var location_reflectivity: Int = 0

    override fun bindAttributes() {
        super.bindAttribute(0, "position")
        super.bindAttribute(1, "textureCoordinates")
        super.bindAttribute(2, "normal")
    }

    override fun getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix")
        location_projectionMatrix = super.getUniformLocation("projectionMatrix")
        location_viewMatrix = super.getUniformLocation("viewMatrix")
        location_lightPosition = super.getUniformLocation("lightPosition")
        location_lightColour = super.getUniformLocation("lightColour")
        location_shineDamper = super.getUniformLocation("shineDamper")
        location_reflectivity = super.getUniformLocation("reflectivity")

    }

    fun loadShineVariables(damper: Float, reflectivity: Float) {
        super.loadFloat(location_shineDamper, damper)
        super.loadFloat(location_reflectivity, reflectivity)
    }

    fun loadTransformationMatrix(matrix: Matrix4f) {
        super.loadMatrix(location_transformationMatrix, matrix)
    }

    fun loadLight(light: Light) {
        super.loadVector(location_lightPosition, light.position)
        super.loadVector(location_lightColour, light.colour)
    }

    fun loadViewMatrix(camera: Camera) {
        val viewMatrix = Maths.createViewMatrix(camera)
        super.loadMatrix(location_viewMatrix, viewMatrix)
    }

    fun loadProjectionMatrix(projection: Matrix4f) {
        super.loadMatrix(location_projectionMatrix, projection)
    }

    companion object {
        private val VERTEX_FILE = "GameEngine/src/shaders/terrainVertexShader.txt"
        private val FRAGMENT_FILE = "GameEngine/src/shaders/terrainFragmentShader.txt"
    }
}
