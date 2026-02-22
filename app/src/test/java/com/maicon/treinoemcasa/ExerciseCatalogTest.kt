package com.maicon.treinoemcasa

import com.maicon.treinoemcasa.data.ExerciseCatalog
import org.junit.Assert.assertTrue
import org.junit.Test

class ExerciseCatalogTest {
    @Test
    fun `catalog should contain at least 100 exercises`() {
        assertTrue(ExerciseCatalog.allExercises.size >= 100)
    }

    @Test
    fun `all exercises should expose scientific references`() {
        assertTrue(ExerciseCatalog.allExercises.all { it.scienceReferences.isNotEmpty() })
    }

    @Test
    fun `push up should include progression metadata`() {
        val pushUp = ExerciseCatalog.allExercises.first { it.id == "push_up" }
        assertTrue(pushUp.regressions.isNotEmpty() || pushUp.progressions.isNotEmpty())
    }

    @Test
    fun `all exercises should expose curated media links`() {
        assertTrue(
            ExerciseCatalog.allExercises.all { exercise ->
                exercise.homeImageUrl.isNotBlank() &&
                    exercise.videoUrl.isNotBlank() &&
                    exercise.videoEmbedUrl.isNotBlank()
            }
        )
    }
}
