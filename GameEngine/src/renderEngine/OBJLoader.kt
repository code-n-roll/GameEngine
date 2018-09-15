package renderEngine

import models.RawModel
import org.lwjgl.util.vector.Vector2f
import org.lwjgl.util.vector.Vector3f
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class OBJLoader {

    companion object {

        fun loadObjModel(fileName: String , loader: Loader): RawModel  {
            val fileReader = FileReader(File("GameEngine/res/$fileName.obj"))

            val reader = BufferedReader(fileReader)
            var line: String?
            var vertices = ArrayList<Vector3f>()
            var textures = ArrayList<Vector2f>()
            var normals = ArrayList<Vector3f>()
            var indices = ArrayList<Int>()
            var verticesArray = floatArrayOf()
            var normalArray = floatArrayOf()
            var textureArray = floatArrayOf()
            var indicesArray = intArrayOf()

            while(true) {
                line = reader.readLine()
                var currentLine = line.split(" ")
                if (line.startsWith("v ")) {
                    val vertex = Vector3f(
                            currentLine[1].toFloat(),
                            currentLine[2].toFloat(),
                            currentLine[3].toFloat())
                    vertices.add(vertex)
                }
                else if (line.startsWith("vt ")) {
                    val texture = Vector2f(
                            currentLine[1].toFloat(),
                            currentLine[2].toFloat())
                    textures.add(texture)
                }
                else if (line.startsWith("vn ")) {
                    val normal = Vector3f(
                            currentLine[1].toFloat(),
                            currentLine[2].toFloat(),
                            currentLine[3].toFloat())
                    normals.add(normal)
                }
                else if (line.startsWith("f ")) {
                    textureArray = FloatArray(vertices.size * 2)
                    normalArray = FloatArray(vertices.size * 3)
                    break
                }
            }

            while(line != null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine()
                    continue
                }
                val currentLine = line.split(" ")
                val vertex1 = currentLine[1].split("/")
                val vertex2 = currentLine[2].split("/")
                val vertex3 = currentLine[3].split("/")

                processVertex(vertex1, indices, textures, normals, textureArray, normalArray)
                processVertex(vertex2, indices, textures, normals, textureArray, normalArray)
                processVertex(vertex3, indices, textures, normals, textureArray, normalArray)

                line = reader.readLine()
            }
            reader.close()

            verticesArray = FloatArray(vertices.size*3)
            indicesArray = IntArray(indices.size)

            var vertexPointer = 0
            for (vertex in vertices) {
                verticesArray[vertexPointer++] = vertex.x
                verticesArray[vertexPointer++] = vertex.y
                verticesArray[vertexPointer++] = vertex.z
            }

            for (index in 0 until indices.size) {
                indicesArray[index] = indices[index]
            }

            return loader.loadToVAO(verticesArray, textureArray, indicesArray)
        }

        fun processVertex(vertexData: List<String>,
                          indices: ArrayList<Int>,
                          textures: List<Vector2f>,
                          normals: List<Vector3f>,
                          textureArray: FloatArray,
                          normalArray: FloatArray) {
            val currentVertexPointer = vertexData[0].toInt() - 1
            indices.add(currentVertexPointer)

            val currentTex = textures[vertexData[1].toInt() - 1]
            textureArray[currentVertexPointer*2] = currentTex.x
            textureArray[currentVertexPointer*2+1] = 1 - currentTex.y

            val currentNorm = normals[vertexData[2].toInt() - 1]
            normalArray[currentVertexPointer*3] = currentNorm.x
            normalArray[currentVertexPointer*3+1] = currentNorm.y
            normalArray[currentVertexPointer*3+2] = currentNorm.z

        }
    }
}