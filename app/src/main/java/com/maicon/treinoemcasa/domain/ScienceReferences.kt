package com.maicon.treinoemcasa.domain

object ScienceReferences {
    const val ACSM_2009 = "ACSM Position Stand (2009): Progression Models in Resistance Training for Healthy Adults."
    const val SCHOENFELD_2010 = "Schoenfeld (2010): Mechanisms of Muscle Hypertrophy and Their Application to Resistance Training."
    const val SCHOENFELD_2017 = "Schoenfeld et al. (2017): Dose-response between resistance training volume and muscle hypertrophy."
    const val GRGIC_2021 = "Grgic et al. (2021): Effects of resistance training frequency on muscular strength and hypertrophy."
    const val RALSTON_2018 = "Ralston et al. (2018): Rest interval effects on strength and hypertrophy outcomes."
    const val LAURSEN_2002 = "Laursen & Jenkins (2002): Scientific basis for high-intensity interval training."
    const val GIBALA_2012 = "Gibala et al. (2012): Physiological adaptations to low-volume interval training."
    const val KRAEMER_2002 = "Kraemer & Ratamess (2002): Fundamentals of resistance training progression and exercise prescription."
    const val NSCA_2021 = "NSCA Guidelines (2021): Program design principles for strength and conditioning."
    const val HELMS_2014 = "Helms et al. (2014): Evidence-based recommendations for natural bodybuilding contest preparation."
    const val LAUERSEN_2014 = "Lauersen et al. (2014): Strength training and injury prevention in sports."

    val foundational = listOf(ACSM_2009, KRAEMER_2002, NSCA_2021)
    val hypertrophy = listOf(SCHOENFELD_2010, SCHOENFELD_2017, RALSTON_2018)
    val strength = listOf(ACSM_2009, GRGIC_2021, RALSTON_2018)
    val conditioning = listOf(LAURSEN_2002, GIBALA_2012, ACSM_2009)
    val injuryPrevention = listOf(LAUERSEN_2014, NSCA_2021, ACSM_2009)
    val bodyComposition = listOf(HELMS_2014, SCHOENFELD_2017, GRGIC_2021)
}
