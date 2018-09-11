package shaders

import entities.Camera
import org.lwjgl.util.vector.Matrix4f
import toolbox.Maths

class StaticShader : ShaderProgram(VERTEX_FILE, FRAGMENT_FILE) {

    private var location_transformationMatrix: Int = 0
    private var location_projectionMatrix: Int = 0
    private var location_viewMatrix: Int = 0

    override fun bindAttribute() {
        super.bindAttribute(0, "position")
        super.bindAttribute(1, "textureCoords")

    }

    override fun getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix")
        location_projectionMatrix = super.getUniformLocation("projectionMatrix")
        location_viewMatrix = super.getUniformLocation("viewMatrix")
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

    companion object {

        private const val VERTEX_FILE = "GameEngine/src/shaders/vertexShader.txt"
        private const val FRAGMENT_FILE = "GameEngine/src/shaders/fragmentShader.txt"
    }
}
