package com.maicon.treinoemcasa.data

import com.maicon.treinoemcasa.domain.FocusArea
import com.maicon.treinoemcasa.domain.MovementPattern

object CuratedExerciseMedia {
    data class MediaLinks(
        val imageUrl: String,
        val videoUrl: String,
        val videoEmbedUrl: String
    )

    private val pushImages = listOf(
        "https://images.pexels.com/photos/4164761/pexels-photo-4164761.jpeg",
        "https://images.pexels.com/photos/414029/pexels-photo-414029.jpeg",
        "https://images.pexels.com/photos/2294354/pexels-photo-2294354.jpeg",
        "https://images.pexels.com/photos/2261477/pexels-photo-2261477.jpeg"
    )

    private val pullImages = listOf(
        "https://images.pexels.com/photos/1552242/pexels-photo-1552242.jpeg",
        "https://images.pexels.com/photos/949126/pexels-photo-949126.jpeg",
        "https://images.pexels.com/photos/3838389/pexels-photo-3838389.jpeg",
        "https://images.pexels.com/photos/136404/pexels-photo-136404.jpeg"
    )

    private val legsImages = listOf(
        "https://images.pexels.com/photos/2294361/pexels-photo-2294361.jpeg",
        "https://images.pexels.com/photos/4498606/pexels-photo-4498606.jpeg",
        "https://images.pexels.com/photos/4662438/pexels-photo-4662438.jpeg",
        "https://images.pexels.com/photos/2780762/pexels-photo-2780762.jpeg"
    )

    private val coreImages = listOf(
        "https://images.pexels.com/photos/416717/pexels-photo-416717.jpeg",
        "https://images.pexels.com/photos/3076509/pexels-photo-3076509.jpeg",
        "https://images.pexels.com/photos/3076516/pexels-photo-3076516.jpeg",
        "https://images.pexels.com/photos/3838384/pexels-photo-3838384.jpeg"
    )

    private val skillImages = listOf(
        "https://images.pexels.com/photos/2294403/pexels-photo-2294403.jpeg",
        "https://images.pexels.com/photos/416754/pexels-photo-416754.jpeg",
        "https://images.pexels.com/photos/1431282/pexels-photo-1431282.jpeg",
        "https://images.pexels.com/photos/3837781/pexels-photo-3837781.jpeg"
    )

    private val mobilityImages = listOf(
        "https://images.pexels.com/photos/3757376/pexels-photo-3757376.jpeg",
        "https://images.pexels.com/photos/3822622/pexels-photo-3822622.jpeg",
        "https://images.pexels.com/photos/317157/pexels-photo-317157.jpeg",
        "https://images.pexels.com/photos/3823039/pexels-photo-3823039.jpeg"
    )

    private val conditioningImages = listOf(
        "https://images.pexels.com/photos/1552252/pexels-photo-1552252.jpeg",
        "https://images.pexels.com/photos/931325/pexels-photo-931325.jpeg",
        "https://images.pexels.com/photos/1954524/pexels-photo-1954524.jpeg",
        "https://images.pexels.com/photos/3076504/pexels-photo-3076504.jpeg"
    )

    private val fallbackImages = listOf(
        "https://images.pexels.com/photos/2261485/pexels-photo-2261485.jpeg",
        "https://images.pexels.com/photos/2294363/pexels-photo-2294363.jpeg",
        "https://images.pexels.com/photos/3253501/pexels-photo-3253501.jpeg"
    )

    private val videoByExerciseId = mapOf(
        "push_up" to "IODxDxX7oi4",
        "push_up_knees" to "jWxvty2KROs",
        "diamond_push_up" to "J0DnG1_S92I",
        "decline_push_up" to "SKPab2YC8BE",
        "pike_push_up" to "qHQ_E-f5278",
        "pull_up" to "eGo4IYlbE5g",
        "assisted_pull_up" to "2f4QfQKQxg4",
        "chin_up" to "brhRXlOhsAM",
        "bodyweight_squat" to "aclHkVaku9U",
        "split_squat" to "2C-uNgKwPLE",
        "pistol_squat_box" to "vM6Xk5lN0Vs",
        "glute_bridge" to "wPM8icPu6H8",
        "plank" to "pSHjTRCQxIw",
        "side_plank" to "K2VljzCC16g",
        "dead_bug" to "4XLEnwUr5yE",
        "hollow_hold" to "LlDNef_Ztsc",
        "burpee" to "TU8QYVW0gDU",
        "jumping_jack" to "iSSAk4XCsRA",
        "mountain_climber" to "nmwgirgXLYM",
        "dumbbell_row" to "roCP6wCXPqo",
        "kettlebell_swing" to "YSxHifyI5-U",
        "l_sit" to "IUZJoSP66HI",
        "muscle_up_strict" to "6Yx0Qf8P1c8",
        "handstand_wall_hold" to "fJjI2-M7w7I"
    )

    fun resolve(
        exerciseId: String,
        movementPattern: MovementPattern,
        focusAreas: Set<FocusArea>,
        fallbackSearchQuery: String
    ): MediaLinks {
        val imagePool = chooseImagePool(movementPattern, focusAreas)
        val imageUrl = imagePool.pickBy(exerciseId)

        val curatedVideoId = videoByExerciseId[exerciseId]
        if (curatedVideoId != null) {
            return MediaLinks(
                imageUrl = imageUrl,
                videoUrl = "https://www.youtube.com/watch?v=$curatedVideoId",
                videoEmbedUrl = "https://www.youtube.com/embed/$curatedVideoId"
            )
        }

        val query = encode(fallbackSearchQuery)
        return MediaLinks(
            imageUrl = imageUrl,
            videoUrl = "https://www.youtube.com/results?search_query=$query",
            videoEmbedUrl = "https://www.youtube.com/embed?listType=search&list=$query"
        )
    }

    private fun chooseImagePool(
        movementPattern: MovementPattern,
        focusAreas: Set<FocusArea>
    ): List<String> {
        return when {
            movementPattern == MovementPattern.HORIZONTAL_PUSH || movementPattern == MovementPattern.VERTICAL_PUSH -> pushImages
            movementPattern == MovementPattern.HORIZONTAL_PULL || movementPattern == MovementPattern.VERTICAL_PULL -> pullImages
            movementPattern == MovementPattern.SQUAT || movementPattern == MovementPattern.LUNGE || movementPattern == MovementPattern.HINGE -> legsImages
            movementPattern == MovementPattern.CORE_ANTI_EXTENSION ||
                movementPattern == MovementPattern.CORE_ANTI_ROTATION ||
                movementPattern == MovementPattern.CORE_FLEXION -> coreImages
            movementPattern == MovementPattern.SKILL_STATIC -> skillImages
            movementPattern == MovementPattern.MOBILITY || focusAreas.contains(FocusArea.MOBILITY) -> mobilityImages
            movementPattern == MovementPattern.CONDITIONING || movementPattern == MovementPattern.PLYOMETRIC -> conditioningImages
            else -> fallbackImages
        }
    }

    private fun encode(raw: String): String {
        return raw.trim()
            .lowercase()
            .replace("ç", "c")
            .replace("á", "a")
            .replace("à", "a")
            .replace("â", "a")
            .replace("ã", "a")
            .replace("é", "e")
            .replace("ê", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ô", "o")
            .replace("õ", "o")
            .replace("ú", "u")
            .replace(Regex("[^a-z0-9 ]"), " ")
            .replace(Regex("\\s+"), "+")
    }

    private fun List<String>.pickBy(seed: String): String {
        if (isEmpty()) return ""
        val raw = seed.hashCode()
        val index = ((raw % size) + size) % size
        return this[index]
    }
}
