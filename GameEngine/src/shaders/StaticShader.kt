package shaders

import entities.Camera
import entities.Light
import org.lwjgl.util.vector.Matrix4f
import toolbox.Maths

class StaticShader : ShaderProgram(VERTEX_FILE, FRAGMENT_FILE) {

    private var location_transformationMatrix: Int = 0
    private var location_projectionMatrix: Int = 0
    private var location_viewMatrix: Int = 0
    private var location_lightPosition: Int = 0
    private var location_lightColor: Int = 0
    private var location_shineDamper: Int = 0
    private var location_reflectivity: Int = 0

    override fun bindAttribute() {
        super.bindAttribute(0, "position")
        super.bindAttribute(1, "textureCoords")
        super.bindAttribute(2, "normal")
    }

    override fun getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix")
        location_projectionMatrix = super.getUniformLocation("projectionMatrix")
        location_viewMatrix = super.getUniformLocation("viewMatrix")
        location_lightColor = super.getUniformLocation("lightColor")
        location_lightPosition = super.getUniformLocation("lightPosition")
//        location_shineDamper = super.getUniformLocation("shineDamper")
//        location_reflectivity = super.getUniformLocation("reflectivity")
    }

    fun loadTransformationMatrix(matrix: Matrix4f) {
        super.loadMatrix(location_transformationMatrix, matrix)
    }

    fun loadProjectionMatrix(projection: Matrix4f) {
        super.loadMatrix(location_projectionMatrix, projection)
    }

    fun loadViewMatrix(camera: Camera) {
        val viewMatrix = Maths.createViewMatrix(camera)
        super.loadMatrix(location_viewMatrix, viewMatrix)
    }

    fun loadLight(light: Light) {
        super.loadVector(location_lightPosition, light.position)
        super.loadVector(location_lightColor, light.color)
    }

    fun loadShineVariables(damper: Float, reflectivity: Float) {
        super.loadFloat(location_shineDamper, damper)
        super.loadFloat(location_reflectivity, reflectivity)
    }

    companion object {

        private const val VERTEX_FILE = "GameEngine/src/shaders/vertexShader.txt"
        private const val FRAGMENT_FILE = "GameEngine/src/shaders/fragmentShader.txt"
    }
}
