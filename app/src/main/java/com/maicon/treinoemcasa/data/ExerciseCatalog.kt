package com.maicon.treinoemcasa.data

import com.maicon.treinoemcasa.domain.EquipmentType
import com.maicon.treinoemcasa.domain.EvidenceLevel
import com.maicon.treinoemcasa.domain.Exercise
import com.maicon.treinoemcasa.domain.ExperienceLevel
import com.maicon.treinoemcasa.domain.FocusArea
import com.maicon.treinoemcasa.domain.MovementPattern
import com.maicon.treinoemcasa.domain.MuscleGroup
import com.maicon.treinoemcasa.domain.ScienceReferences
import com.maicon.treinoemcasa.domain.EquipmentType.*
import com.maicon.treinoemcasa.domain.EvidenceLevel.*
import com.maicon.treinoemcasa.domain.ExperienceLevel.*
import com.maicon.treinoemcasa.domain.FocusArea.*
import com.maicon.treinoemcasa.domain.MovementPattern.*
import com.maicon.treinoemcasa.domain.FocusArea.MOBILITY as FA_MOBILITY
import com.maicon.treinoemcasa.domain.MovementPattern.MOBILITY as MP_MOBILITY
import com.maicon.treinoemcasa.domain.MuscleGroup.BICEPS
import com.maicon.treinoemcasa.domain.MuscleGroup.CALVES
import com.maicon.treinoemcasa.domain.MuscleGroup.CORE
import com.maicon.treinoemcasa.domain.MuscleGroup.FOREARMS
import com.maicon.treinoemcasa.domain.MuscleGroup.GLUTES
import com.maicon.treinoemcasa.domain.MuscleGroup.HAMSTRINGS
import com.maicon.treinoemcasa.domain.MuscleGroup.HIP_FLEXORS
import com.maicon.treinoemcasa.domain.MuscleGroup.LATS
import com.maicon.treinoemcasa.domain.MuscleGroup.LOWER_BACK
import com.maicon.treinoemcasa.domain.MuscleGroup.OBLIQUES
import com.maicon.treinoemcasa.domain.MuscleGroup.QUADS
import com.maicon.treinoemcasa.domain.MuscleGroup.SHOULDERS
import com.maicon.treinoemcasa.domain.MuscleGroup.TRICEPS
import com.maicon.treinoemcasa.domain.MuscleGroup.UPPER_BACK
import com.maicon.treinoemcasa.domain.MuscleGroup.CHEST as MG_CHEST

object ExerciseCatalog {
    val allExercises: List<Exercise> by lazy {
        enrichCatalog(baseExercises + generatedCalisthenicsEncyclopedia())
    }

    private fun focusOf(vararg groups: FocusArea): Set<FocusArea> = groups.toSet()

    private fun musclesOf(vararg groups: MuscleGroup): Set<MuscleGroup> = groups.toSet()

    private val baseExercises = listOf(
        ex(
            id = "push_up_knees",
            name = "Flexão com joelhos",
            description = "Versão regressiva da flexão para construir padrão motor.",
            level = BEGINNER,
            focus = focusOf(CHEST, ARMS),
            equipment = setOf(NONE),
            primary = musclesOf(MG_CHEST, TRICEPS),
            secondary = musclesOf(CORE, SHOULDERS),
            steps = listOf(
                "Apoie joelhos no chão e mãos alinhadas ao peito.",
                "Desça com tronco rígido até o peito se aproximar do solo.",
                "Empurre o chão sem perder alinhamento da lombar."
            ),
            videoSearch = "flexao com joelho tecnica",
            scienceTip = "Treinar com amplitude completa acelera ganho técnico nas primeiras semanas."
        ),
        ex(
            id = "push_up",
            name = "Flexão tradicional",
            description = "Exercício base de empurrar para peitoral, tríceps e core.",
            level = BEGINNER,
            focus = focusOf(CHEST, ARMS),
            equipment = setOf(NONE),
            primary = musclesOf(MG_CHEST, TRICEPS),
            secondary = musclesOf(CORE, SHOULDERS),
            steps = listOf(
                "Mãos no chão um pouco além da largura dos ombros.",
                "Desça mantendo cotovelos em cerca de 45 graus.",
                "Suba contraindo peitoral e mantendo abdômen firme."
            ),
            videoSearch = "push up proper form",
            scienceTip = "Controle de velocidade aumenta tempo sob tensão e estímulo hipertrófico."
        ),
        ex(
            id = "diamond_push_up",
            name = "Flexão diamante",
            description = "Variação de flexão com ênfase em tríceps.",
            level = INTERMEDIATE,
            focus = focusOf(CHEST, ARMS),
            equipment = setOf(NONE),
            primary = musclesOf(TRICEPS, MG_CHEST),
            secondary = musclesOf(SHOULDERS, CORE),
            steps = listOf(
                "Forme um triângulo com as mãos sob o peito.",
                "Desça com controle mantendo cotovelos próximos do tronco.",
                "Empurre o solo até extensão total dos cotovelos."
            ),
            videoSearch = "diamond push up tutorial",
            scienceTip = "Posição fechada aumenta o torque no tríceps em comparação à flexão padrão."
        ),
        ex(
            id = "decline_push_up",
            name = "Flexão declinada",
            description = "Pés elevados para maior exigência de peitoral superior e ombros.",
            level = INTERMEDIATE,
            focus = focusOf(CHEST),
            equipment = setOf(BENCH),
            primary = musclesOf(MG_CHEST, SHOULDERS),
            secondary = musclesOf(TRICEPS, CORE),
            steps = listOf(
                "Apoie os pés em banco/sofá e mãos no chão.",
                "Desça o tronco mantendo alinhamento corporal.",
                "Suba em bloco contraindo peito e deltóides."
            ),
            videoSearch = "decline push up at home",
            scienceTip = "A elevação dos pés aumenta carga relativa nos membros superiores."
        ),
        ex(
            id = "archer_push_up",
            name = "Flexão arqueiro",
            description = "Variação unilateral progressiva para força avançada.",
            level = ADVANCED,
            focus = focusOf(CHEST, ARMS),
            equipment = setOf(NONE),
            primary = musclesOf(MG_CHEST, TRICEPS),
            secondary = musclesOf(CORE, SHOULDERS),
            steps = listOf(
                "Abra bastante a base das mãos.",
                "Desça transferindo mais carga para um lado.",
                "Alterne os lados mantendo controle e amplitude."
            ),
            videoSearch = "archer push up progression",
            scienceTip = "Unilateralidade parcial melhora força específica para movimentos avançados."
        ),
        ex(
            id = "pike_push_up",
            name = "Flexão pike",
            description = "Movimento de empurrar vertical para deltoides e tríceps.",
            level = INTERMEDIATE,
            focus = focusOf(ARMS, CHEST),
            equipment = setOf(NONE),
            primary = musclesOf(SHOULDERS, TRICEPS),
            secondary = musclesOf(CORE, MG_CHEST),
            steps = listOf(
                "Eleve o quadril formando um V invertido.",
                "Desça a cabeça em direção ao solo entre as mãos.",
                "Suba empurrando pelo ombro e tríceps."
            ),
            videoSearch = "pike push up form",
            scienceTip = "A direção vertical aproxima o padrão de desenvolvimento para ombros."
        ),
        ex(
            id = "wall_handstand_push_up",
            name = "Handstand push-up na parede",
            description = "Progressão avançada de força para ombros.",
            level = ADVANCED,
            focus = focusOf(ARMS, CHEST),
            equipment = setOf(NONE),
            primary = musclesOf(SHOULDERS, TRICEPS),
            secondary = musclesOf(CORE, MG_CHEST),
            steps = listOf(
                "Suba em posição de parada de mão com apoio na parede.",
                "Desça até limite seguro de amplitude.",
                "Empurre forte até extensão total dos cotovelos."
            ),
            videoSearch = "wall handstand push up tutorial",
            scienceTip = "Exige alta estabilidade escapular e progressão cuidadosa de volume."
        ),
        ex(
            id = "bench_dips",
            name = "Mergulho em banco",
            description = "Exercício de tríceps usando cadeira ou banco.",
            level = BEGINNER,
            focus = focusOf(ARMS),
            equipment = setOf(BENCH),
            primary = musclesOf(TRICEPS),
            secondary = musclesOf(SHOULDERS, MG_CHEST),
            steps = listOf(
                "Apoie as mãos na borda de um banco.",
                "Desça o corpo flexionando os cotovelos.",
                "Suba estendendo os braços sem projetar ombros à frente."
            ),
            videoSearch = "bench dips form",
            scienceTip = "Amplitude moderada protege ombro em praticantes iniciantes."
        ),
        ex(
            id = "superman",
            name = "Superman",
            description = "Fortalecimento do dorso e região lombar sem equipamento.",
            level = BEGINNER,
            focus = focusOf(BACK),
            equipment = setOf(NONE),
            primary = musclesOf(LOWER_BACK, UPPER_BACK),
            secondary = musclesOf(GLUTES),
            steps = listOf(
                "Deite de barriga para baixo com braços estendidos.",
                "Eleve braços e pernas simultaneamente.",
                "Segure 1-2 segundos e retorne controlando."
            ),
            videoSearch = "superman exercise home",
            scienceTip = "Baixa carga axial com foco em endurance postural da cadeia posterior."
        ),
        ex(
            id = "table_inverted_row",
            name = "Remada invertida na mesa",
            description = "Padrão de puxar horizontal em casa.",
            level = INTERMEDIATE,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(NONE),
            primary = musclesOf(LATS, UPPER_BACK),
            secondary = musclesOf(BICEPS, CORE),
            steps = listOf(
                "Segure firme na borda de uma mesa resistente.",
                "Puxe o peito em direção à mesa.",
                "Desça lentamente mantendo escápulas ativas."
            ),
            videoSearch = "inverted row under table",
            scienceTip = "Remadas horizontais equilibram volume de empurrar e ajudam na saúde do ombro."
        ),
        ex(
            id = "australian_pull_up",
            name = "Barra australiana",
            description = "Remada em barra baixa para costas e bíceps.",
            level = BEGINNER,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(PULL_UP_BAR),
            primary = musclesOf(LATS, UPPER_BACK),
            secondary = musclesOf(BICEPS, FOREARMS),
            steps = listOf(
                "Corpo alinhado sob a barra baixa.",
                "Puxe o peito em direção à barra.",
                "Desça sem perder tensão no core."
            ),
            videoSearch = "australian pull up form",
            scienceTip = "Excelente regressão para construir padrão de tração vertical."
        ),
        ex(
            id = "assisted_pull_up",
            name = "Barra fixa assistida",
            description = "Pull-up com apoio elástico para progressão.",
            level = INTERMEDIATE,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(PULL_UP_BAR, RESISTANCE_BAND),
            primary = musclesOf(LATS, BICEPS),
            secondary = musclesOf(UPPER_BACK, FOREARMS),
            steps = listOf(
                "Prenda a faixa na barra e apoie o pé.",
                "Puxe até o queixo ultrapassar a barra.",
                "Desça lentamente mantendo tensão."
            ),
            videoSearch = "assisted pull up band",
            scienceTip = "Assistência elástica permite praticar técnica com volume útil sem falha precoce."
        ),
        ex(
            id = "pull_up",
            name = "Barra fixa pronada",
            description = "Movimento clássico para dorsais e braços.",
            level = ADVANCED,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(PULL_UP_BAR),
            primary = musclesOf(LATS, BICEPS),
            secondary = musclesOf(UPPER_BACK, FOREARMS, CORE),
            steps = listOf(
                "Segure a barra em pegada pronada.",
                "Puxe com cotovelos em direção ao chão.",
                "Desça em controle total até extensão dos braços."
            ),
            videoSearch = "strict pull up technique",
            scienceTip = "Amplitude completa e excêntrica controlada aumentam eficiência da progressão."
        ),
        ex(
            id = "chin_up",
            name = "Barra fixa supinada",
            description = "Variação com maior participação do bíceps.",
            level = INTERMEDIATE,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(PULL_UP_BAR),
            primary = musclesOf(BICEPS, LATS),
            secondary = musclesOf(UPPER_BACK, FOREARMS),
            steps = listOf(
                "Pegada supinada na largura dos ombros.",
                "Puxe até o queixo ultrapassar a barra.",
                "Desça controlando sem balanço corporal."
            ),
            videoSearch = "chin up form",
            scienceTip = "A pegada supinada costuma facilitar ganho de repetições iniciais."
        ),
        ex(
            id = "resistance_band_row",
            name = "Remada com faixa elástica",
            description = "Remada horizontal com resistência elástica.",
            level = BEGINNER,
            focus = focusOf(BACK),
            equipment = setOf(RESISTANCE_BAND),
            primary = musclesOf(LATS, UPPER_BACK),
            secondary = musclesOf(BICEPS, FOREARMS),
            steps = listOf(
                "Prenda a faixa em ponto fixo à frente.",
                "Puxe os cotovelos para trás aproximando escápulas.",
                "Retorne lentamente mantendo postura neutra."
            ),
            videoSearch = "resistance band row",
            scienceTip = "Tensão contínua da faixa favorece controle e conexão mente-músculo."
        ),
        ex(
            id = "face_pull_band",
            name = "Face pull com faixa",
            description = "Fortalece parte superior das costas e manguito.",
            level = BEGINNER,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(RESISTANCE_BAND),
            primary = musclesOf(UPPER_BACK, SHOULDERS),
            secondary = musclesOf(BICEPS, FOREARMS),
            steps = listOf(
                "Ajuste a faixa na altura do rosto.",
                "Puxe para o rosto abrindo cotovelos.",
                "Volte mantendo controle escapular."
            ),
            videoSearch = "band face pull tutorial",
            scienceTip = "Bom complemento para estabilidade escapular e prevenção de desequilíbrios."
        ),
        ex(
            id = "bodyweight_squat",
            name = "Agachamento livre",
            description = "Movimento base de pernas e glúteos.",
            level = BEGINNER,
            focus = focusOf(LEGS),
            equipment = setOf(NONE),
            primary = musclesOf(QUADS, GLUTES),
            secondary = musclesOf(HAMSTRINGS, CORE),
            steps = listOf(
                "Pés na largura dos ombros.",
                "Desça quadril para trás e para baixo.",
                "Suba pressionando o chão com os pés."
            ),
            videoSearch = "bodyweight squat proper form",
            scienceTip = "Agachamento profundo dentro da mobilidade melhora ativação global de membros inferiores."
        ),
        ex(
            id = "reverse_lunge",
            name = "Avanço reverso",
            description = "Exercício unilateral para controle de quadril e joelho.",
            level = BEGINNER,
            focus = focusOf(LEGS),
            equipment = setOf(NONE),
            primary = musclesOf(QUADS, GLUTES),
            secondary = musclesOf(HAMSTRINGS, CALVES),
            steps = listOf(
                "Dê um passo para trás.",
                "Desça até quase tocar o joelho no chão.",
                "Retorne empurrando com a perna da frente."
            ),
            videoSearch = "reverse lunge form",
            scienceTip = "Padrões unilaterais aumentam estabilidade e reduzem assimetrias."
        ),
        ex(
            id = "split_squat",
            name = "Agachamento búlgaro sem carga",
            description = "Foco unilateral intenso para pernas e glúteos.",
            level = INTERMEDIATE,
            focus = focusOf(LEGS),
            equipment = setOf(BENCH),
            primary = musclesOf(QUADS, GLUTES),
            secondary = musclesOf(HAMSTRINGS, CORE),
            steps = listOf(
                "Apoie o pé traseiro em banco baixo.",
                "Desça mantendo joelho da frente estável.",
                "Suba focando em força na perna da frente."
            ),
            videoSearch = "bulgarian split squat bodyweight",
            scienceTip = "Unilateral elevado aumenta recrutamento de glúteo e quadríceps."
        ),
        ex(
            id = "jump_squat",
            name = "Agachamento com salto",
            description = "Pliometria para potência e condicionamento.",
            level = INTERMEDIATE,
            focus = focusOf(LEGS, FULL_BODY),
            equipment = setOf(NONE),
            primary = musclesOf(QUADS, GLUTES, CALVES),
            secondary = musclesOf(CORE),
            steps = listOf(
                "Realize um agachamento curto.",
                "Salte verticalmente com explosão.",
                "Aterrisse suave e repita sem perder técnica."
            ),
            videoSearch = "jump squat technique",
            scienceTip = "Pliometria melhora taxa de desenvolvimento de força quando bem dosada."
        ),
        ex(
            id = "pistol_squat_box",
            name = "Pistol squat com apoio",
            description = "Progressão de agachamento unilateral avançado.",
            level = ADVANCED,
            focus = focusOf(LEGS),
            equipment = setOf(BENCH),
            primary = musclesOf(QUADS, GLUTES),
            secondary = musclesOf(CORE, CALVES),
            steps = listOf(
                "Use um banco para limitar amplitude.",
                "Desça em uma perna mantendo equilíbrio.",
                "Suba sem colapsar joelho para dentro."
            ),
            videoSearch = "pistol squat progression",
            scienceTip = "Controle excêntrico é crucial para segurança em padrões unilaterais avançados."
        ),
        ex(
            id = "glute_bridge",
            name = "Ponte de glúteo",
            description = "Ativação de cadeia posterior e estabilidade pélvica.",
            level = BEGINNER,
            focus = focusOf(LEGS, FULL_BODY),
            equipment = setOf(NONE),
            primary = musclesOf(GLUTES, HAMSTRINGS),
            secondary = musclesOf(LOWER_BACK, CORE),
            steps = listOf(
                "Deite com joelhos flexionados e pés no chão.",
                "Eleve o quadril contraindo glúteos.",
                "Desça lentamente mantendo tensão."
            ),
            videoSearch = "glute bridge form",
            scienceTip = "Fortalecer glúteos melhora eficiência mecânica de agachamentos e corridas."
        ),
        ex(
            id = "single_leg_hip_thrust",
            name = "Hip thrust unilateral",
            description = "Variação unilateral para glúteos com alta ativação.",
            level = INTERMEDIATE,
            focus = focusOf(LEGS),
            equipment = setOf(BENCH),
            primary = musclesOf(GLUTES),
            secondary = musclesOf(HAMSTRINGS, CORE),
            steps = listOf(
                "Escápulas no banco e um pé no chão.",
                "Eleve o quadril empurrando pelo calcanhar.",
                "Segure no topo por 1 segundo."
            ),
            videoSearch = "single leg hip thrust",
            scienceTip = "Isometrias no topo aumentam estímulo de glúteo máximo."
        ),
        ex(
            id = "calf_raise",
            name = "Elevação de panturrilha",
            description = "Fortalecimento de panturrilhas em pé.",
            level = BEGINNER,
            focus = focusOf(LEGS),
            equipment = setOf(NONE),
            primary = musclesOf(CALVES),
            secondary = musclesOf(FOREARMS),
            steps = listOf(
                "Fique na ponta dos pés.",
                "Suba o máximo possível.",
                "Desça em controle sem relaxar totalmente."
            ),
            videoSearch = "standing calf raise home",
            scienceTip = "Alta frequência e amplitude completa favorecem adaptação da panturrilha."
        ),
        ex(
            id = "nordic_assisted",
            name = "Nórdico assistido",
            description = "Exercício de posterior de coxa para prevenção de lesões.",
            level = ADVANCED,
            focus = focusOf(LEGS),
            equipment = setOf(BENCH),
            primary = musclesOf(HAMSTRINGS),
            secondary = musclesOf(GLUTES, LOWER_BACK),
            steps = listOf(
                "Fixe os pés sob apoio firme.",
                "Desça o tronco lentamente controlando a queda.",
                "Use as mãos para assistência na subida se necessário."
            ),
            videoSearch = "assisted nordic curl",
            scienceTip = "Excêntrico de isquiotibiais é altamente associado à redução de lesões musculares."
        ),
        ex(
            id = "plank",
            name = "Prancha frontal",
            description = "Estabilidade global de core.",
            level = BEGINNER,
            focus = focusOf(ABS, FULL_BODY),
            equipment = setOf(NONE),
            primary = musclesOf(CORE),
            secondary = musclesOf(OBLIQUES, LOWER_BACK),
            steps = listOf(
                "Apoie antebraços e pontas dos pés no chão.",
                "Mantenha corpo alinhado sem arquear lombar.",
                "Respire controlado durante toda a série."
            ),
            videoSearch = "plank proper form",
            scienceTip = "Isometrias de core melhoram transferência de força para movimentos multiarticulares."
        ),
        ex(
            id = "side_plank",
            name = "Prancha lateral",
            description = "Foco em oblíquos e estabilidade lateral do tronco.",
            level = BEGINNER,
            focus = focusOf(ABS),
            equipment = setOf(NONE),
            primary = musclesOf(OBLIQUES),
            secondary = musclesOf(CORE, SHOULDERS),
            steps = listOf(
                "Apoie antebraço e lateral do pé no chão.",
                "Eleve o quadril mantendo linha reta.",
                "Sustente sem rodar tronco para frente."
            ),
            videoSearch = "side plank technique",
            scienceTip = "Treino anti-flexão lateral contribui para saúde da lombar."
        ),
        ex(
            id = "dead_bug",
            name = "Dead bug",
            description = "Controle lombo-pélvico e coordenação do core.",
            level = BEGINNER,
            focus = focusOf(ABS),
            equipment = setOf(NONE),
            primary = musclesOf(CORE),
            secondary = musclesOf(OBLIQUES),
            steps = listOf(
                "Deite com braços para cima e joelhos a 90 graus.",
                "Estenda braço e perna opostos sem tirar lombar do chão.",
                "Alterne os lados de forma controlada."
            ),
            videoSearch = "dead bug exercise",
            scienceTip = "Excelente para aprender bracing abdominal sem sobrecarga axial."
        ),
        ex(
            id = "hollow_hold",
            name = "Hollow hold",
            description = "Isometria avançada de core para calistenia.",
            level = INTERMEDIATE,
            focus = focusOf(ABS),
            equipment = setOf(NONE),
            primary = musclesOf(CORE),
            secondary = musclesOf(OBLIQUES, HIP_FLEXORS),
            steps = listOf(
                "Deite e pressione lombar no solo.",
                "Eleve pernas e ombros alguns centímetros.",
                "Sustente respiração curta sem perder posição."
            ),
            videoSearch = "hollow body hold tutorial",
            scienceTip = "Isometrias em posição alongada aumentam endurance local de core."
        ),
        ex(
            id = "mountain_climber",
            name = "Escalador",
            description = "Cardio + core em ritmo elevado.",
            level = BEGINNER,
            focus = focusOf(ABS, FULL_BODY),
            equipment = setOf(NONE),
            primary = musclesOf(CORE, SHOULDERS),
            secondary = musclesOf(QUADS, HIP_FLEXORS),
            steps = listOf(
                "Comece na posição de flexão alta.",
                "Alterne joelhos em direção ao peito rapidamente.",
                "Mantenha quadril estável e abdômen firme."
            ),
            videoSearch = "mountain climber exercise",
            scienceTip = "Intervalos curtos de alta intensidade elevam gasto energético agudo."
        ),
        ex(
            id = "hanging_knee_raise",
            name = "Elevação de joelhos na barra",
            description = "Trabalho de abdômen e flexores de quadril na barra.",
            level = INTERMEDIATE,
            focus = focusOf(ABS),
            equipment = setOf(PULL_UP_BAR),
            primary = musclesOf(CORE, OBLIQUES),
            secondary = musclesOf(FOREARMS, LATS),
            steps = listOf(
                "Pendure-se na barra com corpo controlado.",
                "Eleve joelhos até 90 graus ou acima.",
                "Desça sem balanço excessivo."
            ),
            videoSearch = "hanging knee raise form",
            scienceTip = "Controle de balanço melhora ativação abdominal efetiva."
        ),
        ex(
            id = "leg_raise",
            name = "Elevação de pernas no solo",
            description = "Fortalecimento abdominal inferior.",
            level = INTERMEDIATE,
            focus = focusOf(ABS),
            equipment = setOf(NONE),
            primary = musclesOf(CORE),
            secondary = musclesOf(OBLIQUES, HIP_FLEXORS),
            steps = listOf(
                "Deite com mãos ao lado do corpo.",
                "Eleve pernas estendidas até 90 graus.",
                "Desça devagar sem tirar lombar do solo."
            ),
            videoSearch = "lying leg raise tutorial",
            scienceTip = "Fase excêntrica lenta aumenta controle lombar e estímulo muscular."
        ),
        ex(
            id = "russian_twist",
            name = "Russian twist",
            description = "Movimento rotacional para oblíquos.",
            level = BEGINNER,
            focus = focusOf(ABS),
            equipment = setOf(NONE, DUMBBELL),
            primary = musclesOf(OBLIQUES),
            secondary = musclesOf(CORE),
            steps = listOf(
                "Sente e incline o tronco para trás.",
                "Gire o tronco de um lado para o outro.",
                "Mantenha abdômen ativo o tempo todo."
            ),
            videoSearch = "russian twist form",
            scienceTip = "Rotação controlada melhora estabilidade dinâmica do tronco."
        ),
        ex(
            id = "reverse_crunch",
            name = "Crunch reverso",
            description = "Exercício de abdominal com ênfase infra.",
            level = BEGINNER,
            focus = focusOf(ABS),
            equipment = setOf(NONE),
            primary = musclesOf(CORE),
            secondary = musclesOf(OBLIQUES),
            steps = listOf(
                "Deite com joelhos flexionados.",
                "Eleve quadris e joelhos em direção ao peito.",
                "Retorne devagar sem impulso."
            ),
            videoSearch = "reverse crunch correct form",
            scienceTip = "Controle da pelve aumenta participação abdominal e reduz compensação."
        ),
        ex(
            id = "burpee",
            name = "Burpee",
            description = "Exercício metabólico completo.",
            level = INTERMEDIATE,
            focus = focusOf(FULL_BODY),
            equipment = setOf(NONE),
            primary = musclesOf(CORE, QUADS, MG_CHEST),
            secondary = musclesOf(SHOULDERS, CALVES),
            steps = listOf(
                "Agache e leve as mãos ao chão.",
                "Projete pés para trás, faça flexão opcional.",
                "Retorne e salte verticalmente."
            ),
            videoSearch = "burpee proper form",
            scienceTip = "Blocos intervalados de burpee elevam VO2 e gasto calórico."
        ),
        ex(
            id = "jumping_jack",
            name = "Polichinelo",
            description = "Cardio clássico para aquecimento ou HIIT.",
            level = BEGINNER,
            focus = focusOf(FULL_BODY),
            equipment = setOf(NONE),
            primary = musclesOf(CALVES, SHOULDERS),
            secondary = musclesOf(CORE),
            steps = listOf(
                "Salte abrindo pernas e elevando braços.",
                "Retorne para posição inicial.",
                "Repita mantendo ritmo constante."
            ),
            videoSearch = "jumping jacks exercise",
            scienceTip = "Excelente para elevação rápida da frequência cardíaca antes de blocos principais."
        ),
        ex(
            id = "high_knees",
            name = "Corrida parada com joelhos altos",
            description = "Condicionamento e coordenação.",
            level = BEGINNER,
            focus = focusOf(FULL_BODY, LEGS),
            equipment = setOf(NONE),
            primary = musclesOf(QUADS, CALVES),
            secondary = musclesOf(CORE, HIP_FLEXORS),
            steps = listOf(
                "Corra no lugar elevando joelhos até a linha do quadril.",
                "Mantenha tronco ereto e braços ativos.",
                "Controle aterrissagem para reduzir impacto."
            ),
            videoSearch = "high knees form",
            scienceTip = "Padrão cíclico de alta cadência melhora resistência anaeróbica."
        ),
        ex(
            id = "bear_crawl",
            name = "Bear crawl",
            description = "Locomoção de corpo inteiro com core ativo.",
            level = INTERMEDIATE,
            focus = focusOf(FULL_BODY),
            equipment = setOf(NONE),
            primary = musclesOf(CORE, SHOULDERS, QUADS),
            secondary = musclesOf(MG_CHEST, GLUTES),
            steps = listOf(
                "Fique em quatro apoios com joelhos próximos do chão.",
                "Avance alternando mão e perna opostas.",
                "Mantenha coluna neutra e quadril baixo."
            ),
            videoSearch = "bear crawl exercise",
            scienceTip = "Deslocamentos quadrúpedes aumentam integração neuromuscular."
        ),
        ex(
            id = "rope_skip",
            name = "Pular corda",
            description = "Cardio eficiente para casa com baixo custo.",
            level = BEGINNER,
            focus = focusOf(FULL_BODY),
            equipment = setOf(JUMP_ROPE),
            primary = musclesOf(CALVES, SHOULDERS),
            secondary = musclesOf(CORE),
            steps = listOf(
                "Mantenha cotovelos próximos ao corpo.",
                "Gire a corda com punhos, não com braços inteiros.",
                "Salte baixo e em ritmo estável."
            ),
            videoSearch = "jump rope technique",
            scienceTip = "Treino com corda melhora coordenação e aptidão cardiorrespiratória."
        ),
        ex(
            id = "dumbbell_thruster",
            name = "Thruster com halteres",
            description = "Agachamento + desenvolvimento em um único movimento.",
            level = INTERMEDIATE,
            focus = focusOf(FULL_BODY, LEGS, ARMS),
            equipment = setOf(DUMBBELL),
            primary = musclesOf(QUADS, SHOULDERS),
            secondary = musclesOf(CORE, TRICEPS, GLUTES),
            steps = listOf(
                "Segure halteres na altura dos ombros.",
                "Faça agachamento completo.",
                "Suba e empurre halteres acima da cabeça."
            ),
            videoSearch = "dumbbell thruster form",
            scienceTip = "Movimentos compostos elevam densidade de treino e gasto energético total."
        ),
        ex(
            id = "kettlebell_swing",
            name = "Kettlebell swing",
            description = "Exercício explosivo de quadril e condicionamento.",
            level = INTERMEDIATE,
            focus = focusOf(FULL_BODY, LEGS),
            equipment = setOf(KETTLEBELL),
            primary = musclesOf(GLUTES, HAMSTRINGS),
            secondary = musclesOf(LOWER_BACK, CORE, SHOULDERS),
            steps = listOf(
                "Incline quadril para trás segurando o kettlebell.",
                "Projete quadril à frente com explosão.",
                "Deixe o peso subir pela inércia até altura do peito."
            ),
            videoSearch = "kettlebell swing tutorial",
            scienceTip = "Padrão de hinge explosivo desenvolve potência da cadeia posterior."
        ),
        ex(
            id = "dumbbell_row",
            name = "Remada unilateral com halter",
            description = "Construção de costas com carga externa.",
            level = BEGINNER,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(DUMBBELL, BENCH),
            primary = musclesOf(LATS, UPPER_BACK),
            secondary = musclesOf(BICEPS, FOREARMS),
            steps = listOf(
                "Apoie joelho e mão no banco.",
                "Puxe o halter em direção ao quadril.",
                "Desça sem perder postura neutra."
            ),
            videoSearch = "one arm dumbbell row",
            scienceTip = "Exercícios unilaterais ajudam a equalizar assimetrias de força."
        ),
        ex(
            id = "dumbbell_floor_press",
            name = "Supino no chão com halter",
            description = "Press horizontal seguro para treino em casa.",
            level = BEGINNER,
            focus = focusOf(CHEST, ARMS),
            equipment = setOf(DUMBBELL),
            primary = musclesOf(MG_CHEST, TRICEPS),
            secondary = musclesOf(SHOULDERS),
            steps = listOf(
                "Deite no chão com halteres ao lado do peito.",
                "Empurre os halteres para cima.",
                "Desça até tríceps tocar levemente o solo."
            ),
            videoSearch = "dumbbell floor press form",
            scienceTip = "Amplitude reduzida protege ombro sem perder estímulo de peitoral e tríceps."
        ),
        ex(
            id = "dumbbell_goblet_squat",
            name = "Agachamento goblet",
            description = "Agachamento com halter à frente do peito.",
            level = BEGINNER,
            focus = focusOf(LEGS),
            equipment = setOf(DUMBBELL),
            primary = musclesOf(QUADS, GLUTES),
            secondary = musclesOf(CORE, HAMSTRINGS),
            steps = listOf(
                "Segure o halter junto ao peito.",
                "Desça em agachamento mantendo coluna neutra.",
                "Suba empurrando o chão com os pés."
            ),
            videoSearch = "goblet squat tutorial",
            scienceTip = "Carga frontal facilita profundidade com melhor controle de tronco."
        ),
        ex(
            id = "dumbbell_romanian_deadlift",
            name = "Stiff com halteres",
            description = "Hinge de quadril para posterior e glúteos.",
            level = INTERMEDIATE,
            focus = focusOf(LEGS, BACK),
            equipment = setOf(DUMBBELL),
            primary = musclesOf(HAMSTRINGS, GLUTES),
            secondary = musclesOf(LOWER_BACK, FOREARMS),
            steps = listOf(
                "Segure halteres à frente das coxas.",
                "Desça o tronco com quadril para trás.",
                "Suba contraindo glúteos sem arredondar lombar."
            ),
            videoSearch = "dumbbell romanian deadlift",
            scienceTip = "Padrão de hinge fortalece cadeia posterior e melhora desempenho em saltos e sprints."
        ),
        ex(
            id = "dumbbell_curl",
            name = "Rosca direta com halter",
            description = "Exercício isolado para bíceps.",
            level = BEGINNER,
            focus = focusOf(ARMS),
            equipment = setOf(DUMBBELL),
            primary = musclesOf(BICEPS),
            secondary = musclesOf(FOREARMS),
            steps = listOf(
                "Braços estendidos ao lado do corpo.",
                "Flexione os cotovelos sem balançar o tronco.",
                "Desça em controle total."
            ),
            videoSearch = "dumbbell biceps curl form",
            scienceTip = "Controle excêntrico de 2-3 segundos aumenta recrutamento efetivo do bíceps."
        ),
        ex(
            id = "hammer_curl",
            name = "Rosca martelo",
            description = "Ênfase em braquial e antebraço.",
            level = BEGINNER,
            focus = focusOf(ARMS),
            equipment = setOf(DUMBBELL),
            primary = musclesOf(BICEPS, FOREARMS),
            secondary = musclesOf(SHOULDERS),
            steps = listOf(
                "Segure halteres com pegada neutra.",
                "Flexione cotovelos sem girar os punhos.",
                "Desça lentamente mantendo tensão."
            ),
            videoSearch = "hammer curl technique",
            scienceTip = "Variações de pegada melhoram desenvolvimento completo do braço."
        ),
        ex(
            id = "overhead_triceps_extension",
            name = "Tríceps acima da cabeça",
            description = "Extensão de tríceps com halter.",
            level = INTERMEDIATE,
            focus = focusOf(ARMS),
            equipment = setOf(DUMBBELL),
            primary = musclesOf(TRICEPS),
            secondary = musclesOf(CORE),
            steps = listOf(
                "Segure halter acima da cabeça com duas mãos.",
                "Flexione cotovelos atrás da cabeça.",
                "Estenda os cotovelos completamente."
            ),
            videoSearch = "overhead dumbbell triceps extension",
            scienceTip = "Trabalhar tríceps em posição alongada potencializa estímulo da porção longa."
        ),
        ex(
            id = "band_biceps_curl",
            name = "Rosca com faixa elástica",
            description = "Alternativa de bíceps sem halteres.",
            level = BEGINNER,
            focus = focusOf(ARMS),
            equipment = setOf(RESISTANCE_BAND),
            primary = musclesOf(BICEPS),
            secondary = musclesOf(FOREARMS),
            steps = listOf(
                "Pise na faixa segurando as pontas.",
                "Flexione os cotovelos mantendo ombros estáveis.",
                "Desça controlando a resistência elástica."
            ),
            videoSearch = "band bicep curl",
            scienceTip = "Tensão crescente da faixa favorece fase final da curva de força."
        ),
        ex(
            id = "band_triceps_pushdown",
            name = "Tríceps pushdown com faixa",
            description = "Trabalho de tríceps com fixação alta da faixa.",
            level = BEGINNER,
            focus = focusOf(ARMS),
            equipment = setOf(RESISTANCE_BAND),
            primary = musclesOf(TRICEPS),
            secondary = musclesOf(FOREARMS),
            steps = listOf(
                "Prenda a faixa em ponto alto.",
                "Empurre para baixo estendendo cotovelos.",
                "Retorne em controle sem mover o tronco."
            ),
            videoSearch = "band triceps pushdown",
            scienceTip = "Boa opção para aumentar volume de tríceps com baixa sobrecarga articular."
        ),
        ex(
            id = "farmer_carry",
            name = "Farmer carry",
            description = "Caminhada carregando pesos para core e pegada.",
            level = INTERMEDIATE,
            focus = focusOf(FULL_BODY, ARMS),
            equipment = setOf(DUMBBELL, KETTLEBELL),
            primary = musclesOf(FOREARMS, CORE, SHOULDERS),
            secondary = musclesOf(QUADS, CALVES),
            steps = listOf(
                "Segure cargas ao lado do corpo.",
                "Caminhe com postura alta e passos curtos.",
                "Evite inclinar o tronco durante o percurso."
            ),
            videoSearch = "farmer carry form",
            scienceTip = "Carregadas melhoram estabilidade global e resistência de pegada."
        ),
        ex(
            id = "tempo_push_up",
            name = "Flexão com tempo controlado",
            description = "Flexão com 3 segundos na descida e pausa no fundo.",
            level = INTERMEDIATE,
            focus = focusOf(CHEST, ARMS),
            equipment = setOf(NONE),
            primary = musclesOf(MG_CHEST, TRICEPS),
            secondary = musclesOf(CORE, SHOULDERS),
            steps = listOf(
                "Desça em 3 segundos.",
                "Segure 1 segundo próximo ao chão.",
                "Suba em 1-2 segundos sem perder postura."
            ),
            videoSearch = "tempo push up",
            scienceTip = "Manipular cadência aumenta intensidade sem necessidade de carga extra."
        ),
        ex(
            id = "tempo_squat",
            name = "Agachamento com pausa",
            description = "Agachamento com isometria no fundo.",
            level = INTERMEDIATE,
            focus = focusOf(LEGS),
            equipment = setOf(NONE),
            primary = musclesOf(QUADS, GLUTES),
            secondary = musclesOf(HAMSTRINGS, CORE),
            steps = listOf(
                "Desça controlando por 3 segundos.",
                "Pausar 1-2 segundos no fundo.",
                "Suba mantendo joelhos alinhados."
            ),
            videoSearch = "tempo squat exercise",
            scienceTip = "Pausas no alongamento aumentam demanda mecânica e controle técnico."
        ),
        ex(
            id = "mobility_flow",
            name = "Flow de mobilidade",
            description = "Sequência de mobilidade para quadril, tornozelo e coluna torácica.",
            level = BEGINNER,
            focus = focusOf(FA_MOBILITY, FULL_BODY),
            equipment = setOf(NONE),
            primary = musclesOf(CORE, GLUTES, SHOULDERS),
            secondary = musclesOf(HAMSTRINGS, CALVES),
            steps = listOf(
                "Faça 5 repetições de world greatest stretch por lado.",
                "Inclua mobilidade de tornozelo e rotações torácicas.",
                "Finalize com respiração diafragmática por 2 minutos."
            ),
            videoSearch = "mobility flow at home",
            scienceTip = "Rotinas curtas de mobilidade melhoram amplitude e qualidade de movimento."
        )
    )

    private fun generatedCalisthenicsEncyclopedia(): List<Exercise> = listOf(
        ex(
            id = "wall_push_up",
            name = "Flexão na parede",
            description = "Entrada regressiva para aprender padrão de empurrar com baixa carga.",
            level = BEGINNER,
            movementPattern = HORIZONTAL_PUSH,
            focus = focusOf(CHEST, ARMS),
            equipment = setOf(NONE),
            primary = musclesOf(MG_CHEST, TRICEPS),
            secondary = musclesOf(SHOULDERS, CORE),
            steps = listOf(
                "Mãos na parede na altura do peito.",
                "Desça o corpo em bloco até quase tocar a parede.",
                "Empurre de volta mantendo abdômen ativo."
            ),
            videoSearch = "wall push up form",
            scienceTip = "Regressões mecânicas facilitam aderência em iniciantes e preservam técnica."
        ),
        ex(
            id = "incline_push_up",
            name = "Flexão inclinada",
            description = "Flexão com mãos elevadas para reduzir a carga relativa.",
            level = BEGINNER,
            movementPattern = HORIZONTAL_PUSH,
            focus = focusOf(CHEST, ARMS),
            equipment = setOf(BENCH),
            primary = musclesOf(MG_CHEST, TRICEPS),
            secondary = musclesOf(SHOULDERS, CORE),
            steps = listOf(
                "Apoie as mãos em banco ou superfície firme.",
                "Desça até o peito se aproximar do apoio.",
                "Suba sem perder alinhamento corporal."
            ),
            videoSearch = "incline push up technique",
            scienceTip = "Ajustar alavanca mantém intensidade adequada para alto volume técnico."
        ),
        ex(
            id = "wide_push_up",
            name = "Flexão aberta",
            description = "Variação de flexão com ênfase no peitoral.",
            level = INTERMEDIATE,
            movementPattern = HORIZONTAL_PUSH,
            focus = focusOf(CHEST),
            equipment = setOf(NONE),
            primary = musclesOf(MG_CHEST),
            secondary = musclesOf(TRICEPS, SHOULDERS, CORE),
            steps = listOf(
                "Posicione mãos mais abertas que a linha dos ombros.",
                "Desça em controle mantendo cotovelos semiflexionados.",
                "Empurre até extensão sem perder postura."
            ),
            videoSearch = "wide push up form",
            scienceTip = "Pequenas variações de pegada redistribuem o torque entre peitoral e tríceps."
        ),
        ex(
            id = "ring_push_up",
            name = "Flexão nas argolas",
            description = "Flexão instável para maior exigência de estabilização escapular.",
            level = INTERMEDIATE,
            movementPattern = HORIZONTAL_PUSH,
            focus = focusOf(CHEST, ARMS),
            equipment = setOf(GYMNASTIC_RINGS),
            primary = musclesOf(MG_CHEST, TRICEPS),
            secondary = musclesOf(SHOULDERS, CORE),
            steps = listOf(
                "Ajuste argolas próximas ao solo.",
                "Desça controlando instabilidade sem abrir excessivamente cotovelos.",
                "Suba mantendo escápulas estáveis."
            ),
            videoSearch = "ring push up form",
            scienceTip = "Superfícies instáveis aumentam demanda de estabilizadores sem elevar muito a carga externa."
        ),
        ex(
            id = "pseudo_planche_push_up",
            name = "Pseudo planche push-up",
            description = "Variação avançada para transferência a progressões de planche.",
            level = ADVANCED,
            movementPattern = HORIZONTAL_PUSH,
            focus = focusOf(CHEST, ARMS),
            equipment = setOf(PARALLETTES, NONE),
            primary = musclesOf(SHOULDERS, MG_CHEST, TRICEPS),
            secondary = musclesOf(CORE, HIP_FLEXORS),
            steps = listOf(
                "Projete ombros à frente das mãos.",
                "Desça com cotovelos próximos ao tronco.",
                "Suba mantendo inclinação anterior do corpo."
            ),
            videoSearch = "pseudo planche push up progression",
            scienceTip = "Deslocar centro de massa à frente aumenta o momento de força nos ombros."
        ),
        ex(
            id = "planche_lean_hold",
            name = "Planche lean hold",
            description = "Isometria preparatória para planche.",
            level = ADVANCED,
            movementPattern = SKILL_STATIC,
            focus = focusOf(ARMS, ABS),
            equipment = setOf(PARALLETTES, NONE),
            primary = musclesOf(SHOULDERS, CORE, TRICEPS),
            secondary = musclesOf(MG_CHEST, HIP_FLEXORS),
            steps = listOf(
                "Mãos no chão ou paralelas com braços estendidos.",
                "Projete ombros à frente das mãos.",
                "Sustente sem perder protração escapular."
            ),
            videoSearch = "planche lean hold",
            scienceTip = "Isometrias específicas melhoram tolerância tecidual e controle neural em skills."
        ),
        ex(
            id = "tuck_planche_hold",
            name = "Tuck planche hold",
            description = "Progressão de planche com joelhos recolhidos.",
            level = ADVANCED,
            movementPattern = SKILL_STATIC,
            focus = focusOf(ARMS, ABS),
            equipment = setOf(PARALLETTES),
            primary = musclesOf(SHOULDERS, TRICEPS, CORE),
            secondary = musclesOf(MG_CHEST, HIP_FLEXORS),
            steps = listOf(
                "Suba em apoio nas paralelas com joelhos ao peito.",
                "Projete ombros à frente para equilibrar.",
                "Sustente com escápulas protraídas."
            ),
            videoSearch = "tuck planche hold progression",
            scienceTip = "Progressões de alavanca permitem ganho de força específica com menor risco."
        ),
        ex(
            id = "parallel_bar_dip",
            name = "Mergulho em paralelas",
            description = "Exercício de empurrar vertical para peitoral e tríceps.",
            level = INTERMEDIATE,
            movementPattern = VERTICAL_PUSH,
            focus = focusOf(CHEST, ARMS),
            equipment = setOf(PARALLETTES, GYMNASTIC_RINGS),
            primary = musclesOf(TRICEPS, MG_CHEST),
            secondary = musclesOf(SHOULDERS, CORE),
            steps = listOf(
                "Apoie-se nas barras com braços estendidos.",
                "Desça com tronco levemente inclinado.",
                "Suba sem colapsar ombros."
            ),
            videoSearch = "parallel bar dip proper form",
            scienceTip = "Amplitude controlada reduz estresse anterior do ombro em mergulhos."
        ),
        ex(
            id = "straight_bar_dip",
            name = "Mergulho na barra",
            description = "Variação de dip útil para progressão de muscle-up.",
            level = ADVANCED,
            movementPattern = VERTICAL_PUSH,
            focus = focusOf(CHEST, ARMS),
            equipment = setOf(PULL_UP_BAR),
            primary = musclesOf(TRICEPS, MG_CHEST, SHOULDERS),
            secondary = musclesOf(CORE),
            steps = listOf(
                "Inicie em apoio por cima da barra.",
                "Desça com controle até ângulo seguro de ombro.",
                "Empurre para voltar ao apoio."
            ),
            videoSearch = "straight bar dip form",
            scienceTip = "Transferência específica para fase final do muscle-up."
        ),
        ex(
            id = "dead_hang",
            name = "Dead hang",
            description = "Suspensão passiva para pegada e descompressão.",
            level = BEGINNER,
            movementPattern = SKILL_STATIC,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(PULL_UP_BAR),
            primary = musclesOf(FOREARMS, LATS),
            secondary = musclesOf(UPPER_BACK, CORE),
            steps = listOf(
                "Segure a barra com pegada confortável.",
                "Relaxe parcialmente ombros sem perder controle.",
                "Sustente respiração calma."
            ),
            videoSearch = "dead hang exercise",
            scienceTip = "Pendurar-se melhora resistência de pegada e tolerância de tecidos de ombro."
        ),
        ex(
            id = "active_hang",
            name = "Active hang",
            description = "Suspensão ativa com depressão escapular.",
            level = BEGINNER,
            movementPattern = SKILL_STATIC,
            focus = focusOf(BACK),
            equipment = setOf(PULL_UP_BAR),
            primary = musclesOf(LATS, UPPER_BACK),
            secondary = musclesOf(FOREARMS, CORE),
            steps = listOf(
                "Pendure-se na barra com braços estendidos.",
                "Ative escápulas puxando ombros para baixo.",
                "Sustente sem flexionar cotovelos."
            ),
            videoSearch = "active hang pull up prep",
            scienceTip = "Controle escapular é determinante para progressão segura de puxadas."
        ),
        ex(
            id = "scapular_pull_up",
            name = "Pull-up escapular",
            description = "Ativação de escápulas antes de barras completas.",
            level = BEGINNER,
            movementPattern = VERTICAL_PULL,
            focus = focusOf(BACK),
            equipment = setOf(PULL_UP_BAR),
            primary = musclesOf(LATS, UPPER_BACK),
            secondary = musclesOf(FOREARMS, BICEPS),
            steps = listOf(
                "Em suspensão, mova escápulas para baixo e para trás.",
                "Eleve levemente o corpo sem dobrar cotovelos.",
                "Retorne ao hang ativo."
            ),
            videoSearch = "scapular pull up form",
            scienceTip = "Pré-ativação escapular melhora mecânica e eficiência do pull-up."
        ),
        ex(
            id = "negative_pull_up",
            name = "Barra fixa negativa",
            description = "Ênfase excêntrica para construir força de tração.",
            level = BEGINNER,
            movementPattern = VERTICAL_PULL,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(PULL_UP_BAR),
            primary = musclesOf(LATS, BICEPS),
            secondary = musclesOf(UPPER_BACK, FOREARMS),
            steps = listOf(
                "Suba ao topo com apoio de caixa/salto.",
                "Desça em 3-5 segundos até extensão dos braços.",
                "Repita mantendo controle total."
            ),
            videoSearch = "negative pull up progression",
            scienceTip = "Treino excêntrico acelera adaptação de força em movimentos de peso corporal."
        ),
        ex(
            id = "feet_elevated_inverted_row",
            name = "Remada invertida pés elevados",
            description = "Progressão da remada horizontal aumentando carga relativa.",
            level = INTERMEDIATE,
            movementPattern = HORIZONTAL_PULL,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(PULL_UP_BAR, BENCH),
            primary = musclesOf(LATS, UPPER_BACK),
            secondary = musclesOf(BICEPS, FOREARMS, CORE),
            steps = listOf(
                "Apoie os pés em banco e corpo sob a barra baixa.",
                "Puxe o peito até a barra.",
                "Desça sem perder alinhamento."
            ),
            videoSearch = "feet elevated inverted row",
            scienceTip = "Aumentar inclinação é forma eficiente de progressão sem carga externa."
        ),
        ex(
            id = "ring_row",
            name = "Remada nas argolas",
            description = "Puxada horizontal com instabilidade controlada.",
            level = BEGINNER,
            movementPattern = HORIZONTAL_PULL,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(GYMNASTIC_RINGS),
            primary = musclesOf(LATS, UPPER_BACK),
            secondary = musclesOf(BICEPS, FOREARMS, CORE),
            steps = listOf(
                "Segure as argolas com corpo inclinado.",
                "Puxe o tronco em direção às mãos.",
                "Retorne lentamente mantendo escápulas ativas."
            ),
            videoSearch = "ring row form",
            scienceTip = "Argolas permitem ajustar dificuldade com a inclinação corporal."
        ),
        ex(
            id = "commando_pull_up",
            name = "Barra comando",
            description = "Variação de barra para força unilateral parcial.",
            level = INTERMEDIATE,
            movementPattern = VERTICAL_PULL,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(PULL_UP_BAR),
            primary = musclesOf(LATS, BICEPS, FOREARMS),
            secondary = musclesOf(UPPER_BACK, CORE),
            steps = listOf(
                "Segure a barra em pegada mista (uma mão à frente da outra).",
                "Puxe levando a cabeça para o lado da barra.",
                "Alterne lado dominante a cada série."
            ),
            videoSearch = "commando pull up form",
            scienceTip = "Variações assimétricas ajudam na transição para exercícios unilaterais."
        ),
        ex(
            id = "archer_pull_up",
            name = "Barra arqueiro",
            description = "Progressão unilateral avançada de tração.",
            level = ADVANCED,
            movementPattern = VERTICAL_PULL,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(PULL_UP_BAR),
            primary = musclesOf(LATS, BICEPS),
            secondary = musclesOf(UPPER_BACK, FOREARMS, CORE),
            steps = listOf(
                "Puxe o corpo deslocando o queixo para um lado.",
                "Mantenha um braço mais estendido para reduzir assistência.",
                "Alterne os lados com controle."
            ),
            videoSearch = "archer pull up progression",
            scienceTip = "Progressões unilaterais melhoram força relativa para movimentos avançados."
        ),
        ex(
            id = "chest_to_bar_pull_up",
            name = "Barra peito na barra",
            description = "Tração explosiva com amplitude elevada.",
            level = ADVANCED,
            movementPattern = VERTICAL_PULL,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(PULL_UP_BAR),
            primary = musclesOf(LATS, UPPER_BACK, BICEPS),
            secondary = musclesOf(FOREARMS, CORE),
            steps = listOf(
                "Inicie em hang ativo com core rígido.",
                "Puxe com explosão até encostar peito na barra.",
                "Desça controladamente sem balanço excessivo."
            ),
            videoSearch = "chest to bar pull up technique",
            scienceTip = "Trações explosivas desenvolvem potência útil para skills como muscle-up."
        ),
        ex(
            id = "typewriter_pull_up",
            name = "Barra typewriter",
            description = "Tração avançada combinando força e controle lateral.",
            level = ADVANCED,
            movementPattern = VERTICAL_PULL,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(PULL_UP_BAR),
            primary = musclesOf(LATS, BICEPS, UPPER_BACK),
            secondary = musclesOf(FOREARMS, CORE),
            steps = listOf(
                "Suba em barra com queixo acima da barra.",
                "Desloque lateralmente de um lado para outro.",
                "Retorne e desça em controle."
            ),
            videoSearch = "typewriter pull up",
            scienceTip = "A combinação de isometria e deslocamento lateral eleva demanda neuromuscular."
        ),
        ex(
            id = "muscle_up_transition_band",
            name = "Transição de muscle-up com faixa",
            description = "Drill técnico da fase de transição do muscle-up.",
            level = ADVANCED,
            movementPattern = MIXED,
            focus = focusOf(BACK, ARMS, CHEST),
            equipment = setOf(PULL_UP_BAR, RESISTANCE_BAND),
            primary = musclesOf(LATS, TRICEPS, MG_CHEST),
            secondary = musclesOf(BICEPS, SHOULDERS, CORE),
            steps = listOf(
                "Use faixa para aliviar parte do peso corporal.",
                "Pratique puxada alta e rotação de punhos sobre a barra.",
                "Finalize em apoio acima da barra."
            ),
            videoSearch = "band muscle up transition drill",
            scienceTip = "Segmentar a habilidade em partes acelera aprendizado motor complexo."
        ),
        ex(
            id = "muscle_up_strict",
            name = "Muscle-up estrito",
            description = "Skill avançada de tração e empurrar em sequência.",
            level = ADVANCED,
            movementPattern = MIXED,
            focus = focusOf(BACK, ARMS, CHEST),
            equipment = setOf(PULL_UP_BAR),
            primary = musclesOf(LATS, TRICEPS, MG_CHEST, SHOULDERS),
            secondary = musclesOf(BICEPS, FOREARMS, CORE),
            steps = listOf(
                "Execute puxada explosiva acima da barra.",
                "Transicione rapidamente para posição de dip.",
                "Finalize com extensão completa dos cotovelos."
            ),
            videoSearch = "strict muscle up tutorial",
            scienceTip = "Requer alta força relativa e coordenação intermuscular."
        ),
        ex(
            id = "air_squat_pause",
            name = "Agachamento com pausa longa",
            description = "Agachamento com 3 segundos no fundo para controle técnico.",
            level = BEGINNER,
            movementPattern = SQUAT,
            focus = focusOf(LEGS),
            equipment = setOf(NONE),
            primary = musclesOf(QUADS, GLUTES),
            secondary = musclesOf(HAMSTRINGS, CORE),
            steps = listOf(
                "Desça em agachamento profundo.",
                "Segure 3 segundos mantendo postura neutra.",
                "Suba com controle."
            ),
            videoSearch = "pause squat bodyweight",
            scienceTip = "Pausas melhoram estabilidade e controle em amplitudes críticas."
        ),
        ex(
            id = "cossack_squat",
            name = "Agachamento cossaco",
            description = "Variação lateral para mobilidade e força unilateral.",
            level = INTERMEDIATE,
            movementPattern = LUNGE,
            focus = focusOf(LEGS, FA_MOBILITY),
            equipment = setOf(NONE),
            primary = musclesOf(QUADS, GLUTES),
            secondary = musclesOf(HAMSTRINGS, CALVES, CORE),
            steps = listOf(
                "Abra bem a base dos pés.",
                "Desça lateralmente mantendo uma perna estendida.",
                "Retorne ao centro e repita para o outro lado."
            ),
            videoSearch = "cossack squat tutorial",
            scienceTip = "Planos de movimento variados melhoram controle articular e mobilidade funcional."
        ),
        ex(
            id = "shrimp_squat_assisted",
            name = "Shrimp squat assistido",
            description = "Progressão unilateral para força de quadríceps.",
            level = INTERMEDIATE,
            movementPattern = LUNGE,
            focus = focusOf(LEGS),
            equipment = setOf(NONE),
            primary = musclesOf(QUADS, GLUTES),
            secondary = musclesOf(CORE, CALVES),
            steps = listOf(
                "Segure apoio leve para equilíbrio.",
                "Desça em uma perna com perna oposta flexionada atrás.",
                "Suba controlando alinhamento do joelho."
            ),
            videoSearch = "assisted shrimp squat",
            scienceTip = "Progressões unilaterais aumentam força relativa sem carga externa."
        ),
        ex(
            id = "shrimp_squat",
            name = "Shrimp squat",
            description = "Unilateral avançado para pernas e estabilidade.",
            level = ADVANCED,
            movementPattern = LUNGE,
            focus = focusOf(LEGS),
            equipment = setOf(NONE),
            primary = musclesOf(QUADS, GLUTES),
            secondary = musclesOf(CORE, CALVES),
            steps = listOf(
                "Segure o pé de trás com a mão do mesmo lado.",
                "Desça controlando o tronco ereto.",
                "Suba sem usar impulso."
            ),
            videoSearch = "shrimp squat progression",
            scienceTip = "Alto controle excêntrico favorece ganho de força e estabilidade."
        ),
        ex(
            id = "sissy_squat_assisted",
            name = "Sissy squat assistido",
            description = "Ênfase em quadríceps com apoio para segurança.",
            level = INTERMEDIATE,
            movementPattern = SQUAT,
            focus = focusOf(LEGS),
            equipment = setOf(NONE),
            primary = musclesOf(QUADS),
            secondary = musclesOf(CORE, CALVES),
            steps = listOf(
                "Segure apoio fixo à frente.",
                "Projete joelhos à frente mantendo quadril estendido.",
                "Retorne contraindo quadríceps."
            ),
            videoSearch = "assisted sissy squat",
            scienceTip = "Séries controladas minimizam estresse patelofemoral em progressões."
        ),
        ex(
            id = "step_up_bench",
            name = "Step-up no banco",
            description = "Movimento unilateral funcional para membros inferiores.",
            level = BEGINNER,
            movementPattern = LUNGE,
            focus = focusOf(LEGS),
            equipment = setOf(BENCH),
            primary = musclesOf(QUADS, GLUTES),
            secondary = musclesOf(CALVES, CORE),
            steps = listOf(
                "Apoie um pé sobre banco estável.",
                "Suba empurrando o banco com a perna da frente.",
                "Desça em controle e alterne."
            ),
            videoSearch = "bench step up form",
            scienceTip = "Treinos unilaterais têm alta transferência para tarefas do dia a dia."
        ),
        ex(
            id = "broad_jump",
            name = "Salto horizontal",
            description = "Pliometria para potência de quadril e pernas.",
            level = INTERMEDIATE,
            movementPattern = PLYOMETRIC,
            focus = focusOf(LEGS, FULL_BODY),
            equipment = setOf(NONE),
            primary = musclesOf(GLUTES, QUADS, CALVES),
            secondary = musclesOf(HAMSTRINGS, CORE),
            steps = listOf(
                "Agache curto com balanço de braços.",
                "Salte à frente com máxima distância segura.",
                "Aterrisse macio e estabilize."
            ),
            videoSearch = "broad jump technique",
            scienceTip = "Pliometria bem dosada melhora potência e eficiência neuromuscular."
        ),
        ex(
            id = "wall_sit",
            name = "Wall sit",
            description = "Isometria para quadríceps e tolerância local à fadiga.",
            level = BEGINNER,
            movementPattern = SKILL_STATIC,
            focus = focusOf(LEGS),
            equipment = setOf(NONE),
            primary = musclesOf(QUADS),
            secondary = musclesOf(GLUTES, CALVES),
            steps = listOf(
                "Encoste as costas na parede.",
                "Desça até joelhos próximos de 90 graus.",
                "Sustente com respiração controlada."
            ),
            videoSearch = "wall sit exercise",
            scienceTip = "Isometrias são úteis para aumentar tolerância de esforço local sem impacto."
        ),
        ex(
            id = "reverse_nordic",
            name = "Reverse nordic",
            description = "Movimento avançado para quadríceps em alongamento.",
            level = ADVANCED,
            movementPattern = HINGE,
            focus = focusOf(LEGS),
            equipment = setOf(NONE),
            primary = musclesOf(QUADS),
            secondary = musclesOf(HIP_FLEXORS, CORE),
            steps = listOf(
                "Ajoelhe com tronco alinhado.",
                "Incline o corpo para trás mantendo quadril estendido.",
                "Retorne contraindo quadríceps."
            ),
            videoSearch = "reverse nordic curl",
            scienceTip = "Trabalho em posição alongada pode ampliar estímulo hipertrófico."
        ),
        ex(
            id = "single_leg_rdl_bodyweight",
            name = "Stiff unilateral sem carga",
            description = "Hinge unilateral para cadeia posterior e equilíbrio.",
            level = BEGINNER,
            movementPattern = HINGE,
            focus = focusOf(LEGS, BACK),
            equipment = setOf(NONE),
            primary = musclesOf(HAMSTRINGS, GLUTES),
            secondary = musclesOf(CORE, LOWER_BACK),
            steps = listOf(
                "Fique em uma perna e incline o tronco à frente.",
                "Mantenha coluna neutra e quadril alinhado.",
                "Retorne contraindo glúteos."
            ),
            videoSearch = "single leg rdl bodyweight",
            scienceTip = "Padrões unilaterais melhoram controle motor de quadril e tornozelo."
        ),
        ex(
            id = "good_morning_band",
            name = "Good morning com faixa",
            description = "Hinge de quadril com resistência elástica.",
            level = BEGINNER,
            movementPattern = HINGE,
            focus = focusOf(LEGS, BACK),
            equipment = setOf(RESISTANCE_BAND),
            primary = musclesOf(HAMSTRINGS, GLUTES, LOWER_BACK),
            secondary = musclesOf(CORE),
            steps = listOf(
                "Pise na faixa e apoie atrás do pescoço/trapézio.",
                "Empurre quadril para trás mantendo coluna neutra.",
                "Suba contraindo glúteos."
            ),
            videoSearch = "band good morning exercise",
            scienceTip = "Faixas permitem progressão gradual em padrões de hinge."
        ),
        ex(
            id = "hollow_rock",
            name = "Hollow rock",
            description = "Progressão dinâmica do hollow hold para core avançado.",
            level = INTERMEDIATE,
            movementPattern = CORE_ANTI_EXTENSION,
            focus = focusOf(ABS),
            equipment = setOf(NONE),
            primary = musclesOf(CORE, HIP_FLEXORS),
            secondary = musclesOf(OBLIQUES),
            steps = listOf(
                "Entre na posição de hollow hold.",
                "Realize balanço curto para frente e para trás.",
                "Mantenha lombar em contato com o solo."
            ),
            videoSearch = "hollow rock exercise",
            scienceTip = "Progressões dinâmicas elevam demanda de estabilidade do tronco."
        ),
        ex(
            id = "tuck_l_sit",
            name = "Tuck L-sit",
            description = "Isometria preparatória para L-sit completo.",
            level = INTERMEDIATE,
            movementPattern = SKILL_STATIC,
            focus = focusOf(ABS, ARMS),
            equipment = setOf(PARALLETTES, BENCH),
            primary = musclesOf(CORE, HIP_FLEXORS, SHOULDERS),
            secondary = musclesOf(TRICEPS),
            steps = listOf(
                "Apoie-se nas paralelas com braços estendidos.",
                "Eleve os joelhos em direção ao peito.",
                "Sustente sem perder depressão escapular."
            ),
            videoSearch = "tuck l sit progression",
            scienceTip = "Isometrias com alavanca reduzida aceleram progressão para L-sit."
        ),
        ex(
            id = "l_sit",
            name = "L-sit",
            description = "Skill clássica de força isométrica de core e ombros.",
            level = ADVANCED,
            movementPattern = SKILL_STATIC,
            focus = focusOf(ABS, ARMS),
            equipment = setOf(PARALLETTES),
            primary = musclesOf(CORE, HIP_FLEXORS, SHOULDERS),
            secondary = musclesOf(TRICEPS, QUADS),
            steps = listOf(
                "Em apoio nas paralelas, estenda joelhos à frente.",
                "Mantenha quadril elevado e pernas paralelas ao solo.",
                "Sustente sem encolher ombros."
            ),
            videoSearch = "l sit progression",
            scienceTip = "Exige alta rigidez de tronco e controle de cintura escapular."
        ),
        ex(
            id = "v_up",
            name = "V-up",
            description = "Flexão simultânea de tronco e quadril.",
            level = INTERMEDIATE,
            movementPattern = CORE_FLEXION,
            focus = focusOf(ABS),
            equipment = setOf(NONE),
            primary = musclesOf(CORE, HIP_FLEXORS),
            secondary = musclesOf(OBLIQUES),
            steps = listOf(
                "Deite com braços acima da cabeça.",
                "Eleve tronco e pernas ao mesmo tempo.",
                "Retorne em controle sem perder lombar."
            ),
            videoSearch = "v up abs exercise",
            scienceTip = "Movimentos dinâmicos de flexão podem compor volume complementar de abdômen."
        ),
        ex(
            id = "bicycle_crunch",
            name = "Bicicleta abdominal",
            description = "Exercício dinâmico com rotação de tronco.",
            level = BEGINNER,
            movementPattern = CORE_ANTI_ROTATION,
            focus = focusOf(ABS),
            equipment = setOf(NONE),
            primary = musclesOf(OBLIQUES, CORE),
            secondary = musclesOf(HIP_FLEXORS),
            steps = listOf(
                "Deite e leve mãos atrás da cabeça sem puxar pescoço.",
                "Aproxime cotovelo e joelho opostos alternando lados.",
                "Mantenha movimento fluido e controlado."
            ),
            videoSearch = "bicycle crunch form",
            scienceTip = "Combina trabalho de flexão e rotação controlada para oblíquos."
        ),
        ex(
            id = "ab_wheel_rollout",
            name = "Ab wheel rollout",
            description = "Movimento avançado anti-extensão para core.",
            level = ADVANCED,
            movementPattern = CORE_ANTI_EXTENSION,
            focus = focusOf(ABS),
            equipment = setOf(AB_WHEEL),
            primary = musclesOf(CORE),
            secondary = musclesOf(SHOULDERS, LATS, HIP_FLEXORS),
            steps = listOf(
                "Ajoelhe segurando a roda abdominal.",
                "Role à frente mantendo lombar neutra.",
                "Retorne tracionando pelo abdômen."
            ),
            videoSearch = "ab wheel rollout technique",
            scienceTip = "Alto estímulo anti-extensão com forte transferência para estabilidade global."
        ),
        ex(
            id = "dragon_flag_tuck",
            name = "Dragon flag tuck",
            description = "Progressão do dragon flag com joelhos flexionados.",
            level = ADVANCED,
            movementPattern = CORE_ANTI_EXTENSION,
            focus = focusOf(ABS),
            equipment = setOf(BENCH),
            primary = musclesOf(CORE),
            secondary = musclesOf(LATS, HIP_FLEXORS),
            steps = listOf(
                "Segure apoio firme atrás da cabeça.",
                "Eleve tronco e quadril em bloco com joelhos recolhidos.",
                "Desça lentamente sem perder alinhamento."
            ),
            videoSearch = "dragon flag tuck progression",
            scienceTip = "Excêntrica controlada melhora força específica em alavancas longas."
        ),
        ex(
            id = "dragon_flag_negative",
            name = "Dragon flag negativo",
            description = "Versão excêntrica avançada do dragon flag.",
            level = ADVANCED,
            movementPattern = CORE_ANTI_EXTENSION,
            focus = focusOf(ABS),
            equipment = setOf(BENCH),
            primary = musclesOf(CORE),
            secondary = musclesOf(LATS, HIP_FLEXORS),
            steps = listOf(
                "Comece no topo com corpo alinhado.",
                "Desça lentamente em 4-6 segundos.",
                "Interrompa antes de perder controle lombar."
            ),
            videoSearch = "dragon flag negative",
            scienceTip = "Ênfase excêntrica é estratégia sólida para progressão em skills avançadas."
        ),
        ex(
            id = "pallof_press_band",
            name = "Pallof press com faixa",
            description = "Exercício anti-rotação para estabilidade de core.",
            level = BEGINNER,
            movementPattern = CORE_ANTI_ROTATION,
            focus = focusOf(ABS),
            equipment = setOf(RESISTANCE_BAND),
            primary = musclesOf(CORE, OBLIQUES),
            secondary = musclesOf(SHOULDERS),
            steps = listOf(
                "Prenda a faixa na altura do peito.",
                "Segure a faixa junto ao esterno e estenda os braços.",
                "Resista à rotação do tronco."
            ),
            videoSearch = "pallof press band",
            scienceTip = "Trabalhos anti-rotação ajudam na estabilidade lombopélvica em tarefas funcionais."
        ),
        ex(
            id = "handstand_wall_hold",
            name = "Parada de mão na parede",
            description = "Skill estática para ombros, core e controle corporal.",
            level = INTERMEDIATE,
            movementPattern = SKILL_STATIC,
            focus = focusOf(ARMS, ABS),
            equipment = setOf(NONE),
            primary = musclesOf(SHOULDERS, CORE),
            secondary = musclesOf(TRICEPS, FOREARMS),
            steps = listOf(
                "Suba em handstand com suporte da parede.",
                "Ative glúteos e abdômen para manter linha reta.",
                "Sustente respirando curto."
            ),
            videoSearch = "wall handstand hold",
            scienceTip = "Isometrias invertidas melhoram estabilidade escapular e consciência corporal."
        ),
        ex(
            id = "wall_walk",
            name = "Wall walk",
            description = "Drill de progressão para handstand.",
            level = INTERMEDIATE,
            movementPattern = MIXED,
            focus = focusOf(ARMS, ABS, FULL_BODY),
            equipment = setOf(NONE),
            primary = musclesOf(SHOULDERS, CORE, TRICEPS),
            secondary = musclesOf(MG_CHEST, HIP_FLEXORS),
            steps = listOf(
                "Inicie em prancha com pés na parede.",
                "Caminhe com mãos para trás aproximando o corpo da parede.",
                "Retorne controlando a descida."
            ),
            videoSearch = "wall walk handstand progression",
            scienceTip = "Progressões dinâmicas facilitam transição para habilidades invertidas."
        ),
        ex(
            id = "frog_stand",
            name = "Frog stand",
            description = "Skill de equilíbrio em apoio das mãos (base para crow).",
            level = INTERMEDIATE,
            movementPattern = SKILL_STATIC,
            focus = focusOf(ARMS, ABS),
            equipment = setOf(NONE),
            primary = musclesOf(SHOULDERS, CORE, FOREARMS),
            secondary = musclesOf(TRICEPS),
            steps = listOf(
                "Apoie mãos no chão e joelhos nos tríceps.",
                "Projete peso para frente até tirar os pés do solo.",
                "Sustente com olhar fixo no chão."
            ),
            videoSearch = "frog stand balance",
            scienceTip = "Treinos de equilíbrio em apoio melhoram coordenação e controle de centro de massa."
        ),
        ex(
            id = "front_lever_tuck",
            name = "Front lever tuck",
            description = "Progressão inicial de front lever.",
            level = ADVANCED,
            movementPattern = SKILL_STATIC,
            focus = focusOf(BACK, ABS),
            equipment = setOf(PULL_UP_BAR, GYMNASTIC_RINGS),
            primary = musclesOf(LATS, CORE, UPPER_BACK),
            secondary = musclesOf(FOREARMS, BICEPS),
            steps = listOf(
                "Pendure-se e traga joelhos ao peito.",
                "Incline o tronco para trás até ficar paralelo ao solo.",
                "Sustente com escápulas deprimidas."
            ),
            videoSearch = "front lever tuck hold progression",
            scienceTip = "Progressões de alavanca controlam sobrecarga em skills de alta exigência."
        ),
        ex(
            id = "front_lever_one_leg",
            name = "Front lever uma perna",
            description = "Progressão intermediária para front lever completo.",
            level = ADVANCED,
            movementPattern = SKILL_STATIC,
            focus = focusOf(BACK, ABS),
            equipment = setOf(PULL_UP_BAR, GYMNASTIC_RINGS),
            primary = musclesOf(LATS, CORE, UPPER_BACK),
            secondary = musclesOf(FOREARMS, BICEPS),
            steps = listOf(
                "Entre em posição de front lever tuck.",
                "Estenda uma perna mantendo quadril alinhado.",
                "Alterne perna dominante entre séries."
            ),
            videoSearch = "front lever one leg progression",
            scienceTip = "Progressão unilateral reduz alavanca e permite acumular tempo sob tensão."
        ),
        ex(
            id = "back_lever_tuck",
            name = "Back lever tuck",
            description = "Progressão inicial de back lever.",
            level = ADVANCED,
            movementPattern = SKILL_STATIC,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(GYMNASTIC_RINGS, PULL_UP_BAR),
            primary = musclesOf(SHOULDERS, LATS, MG_CHEST),
            secondary = musclesOf(BICEPS, CORE),
            steps = listOf(
                "A partir da posição invertida, recolha joelhos.",
                "Estenda o corpo para trás gradualmente.",
                "Sustente poucos segundos com segurança."
            ),
            videoSearch = "back lever tuck progression",
            scienceTip = "Back lever exige progressão cautelosa para tecidos do ombro e cotovelo."
        ),
        ex(
            id = "skin_the_cat",
            name = "Skin the cat",
            description = "Mobilidade e força de ombro em argolas/barra.",
            level = ADVANCED,
            movementPattern = MP_MOBILITY,
            focus = focusOf(BACK, ARMS, FA_MOBILITY),
            equipment = setOf(GYMNASTIC_RINGS, PULL_UP_BAR),
            primary = musclesOf(SHOULDERS, LATS, BICEPS),
            secondary = musclesOf(CORE, FOREARMS),
            steps = listOf(
                "Suba em suspensão e eleve joelhos.",
                "Passe pernas entre os braços girando para trás.",
                "Retorne com controle sem forçar amplitude."
            ),
            videoSearch = "skin the cat rings progression",
            scienceTip = "Amplitudes extremas devem ser progredidas gradualmente para segurança articular."
        ),
        ex(
            id = "jump_lunge",
            name = "Afundo com salto",
            description = "Pliometria unilateral para potência metabólica.",
            level = INTERMEDIATE,
            movementPattern = PLYOMETRIC,
            focus = focusOf(LEGS, FULL_BODY),
            equipment = setOf(NONE),
            primary = musclesOf(QUADS, GLUTES, CALVES),
            secondary = musclesOf(CORE, HAMSTRINGS),
            steps = listOf(
                "Inicie em posição de afundo.",
                "Salte trocando as pernas no ar.",
                "Aterrisse com joelhos alinhados."
            ),
            videoSearch = "jump lunge technique",
            scienceTip = "Pliometria unilateral melhora potência e estabilidade em altas velocidades."
        ),
        ex(
            id = "squat_thrust",
            name = "Squat thrust",
            description = "Condicionamento de corpo inteiro sem salto vertical alto.",
            level = BEGINNER,
            movementPattern = CONDITIONING,
            focus = focusOf(FULL_BODY),
            equipment = setOf(NONE),
            primary = musclesOf(CORE, QUADS, SHOULDERS),
            secondary = musclesOf(MG_CHEST, CALVES),
            steps = listOf(
                "Agache levando as mãos ao chão.",
                "Projete os pés para trás e retorne rapidamente.",
                "Levante mantendo ritmo estável."
            ),
            videoSearch = "squat thrust form",
            scienceTip = "Boa opção metabólica com menor impacto do que burpee com salto."
        ),
        ex(
            id = "weighted_push_up_vest",
            name = "Flexão com colete de peso",
            description = "Progressão de sobrecarga para flexão.",
            level = ADVANCED,
            movementPattern = HORIZONTAL_PUSH,
            focus = focusOf(CHEST, ARMS),
            equipment = setOf(WEIGHT_VEST),
            primary = musclesOf(MG_CHEST, TRICEPS),
            secondary = musclesOf(SHOULDERS, CORE),
            steps = listOf(
                "Vista o colete ajustado ao corpo.",
                "Execute flexões em amplitude completa.",
                "Mantenha coluna neutra sem compensações."
            ),
            videoSearch = "weighted push up vest",
            scienceTip = "Sobrecarga progressiva continua sendo princípio central para ganho de força/hipertrofia."
        ),
        ex(
            id = "weighted_pull_up_vest",
            name = "Barra fixa com colete",
            description = "Progressão avançada de força de tração.",
            level = ADVANCED,
            movementPattern = VERTICAL_PULL,
            focus = focusOf(BACK, ARMS),
            equipment = setOf(PULL_UP_BAR, WEIGHT_VEST),
            primary = musclesOf(LATS, BICEPS, UPPER_BACK),
            secondary = musclesOf(FOREARMS, CORE),
            steps = listOf(
                "Vista colete de peso com ajuste firme.",
                "Puxe até queixo ultrapassar a barra.",
                "Desça controlando a fase excêntrica."
            ),
            videoSearch = "weighted pull up form",
            scienceTip = "Tração com carga externa é estratégia sólida para progressão de força máxima."
        ),
        ex(
            id = "thoracic_rotation_flow",
            name = "Flow de rotação torácica",
            description = "Mobilidade de coluna torácica para melhorar padrão de empurrar/puxar.",
            level = BEGINNER,
            movementPattern = MP_MOBILITY,
            focus = focusOf(FA_MOBILITY, FULL_BODY),
            equipment = setOf(NONE),
            primary = musclesOf(UPPER_BACK, SHOULDERS),
            secondary = musclesOf(CORE),
            steps = listOf(
                "Em quatro apoios, posicione uma mão atrás da cabeça.",
                "Gire cotovelo para cima e para baixo em amplitude confortável.",
                "Repita para ambos os lados."
            ),
            videoSearch = "thoracic rotation mobility",
            scienceTip = "Melhorar mobilidade torácica pode favorecer mecânica de ombro."
        ),
        ex(
            id = "ankle_dorsiflexion_rock",
            name = "Rock de dorsiflexão de tornozelo",
            description = "Mobilidade de tornozelo para agachamentos mais eficientes.",
            level = BEGINNER,
            movementPattern = MP_MOBILITY,
            focus = focusOf(FA_MOBILITY, LEGS),
            equipment = setOf(NONE),
            primary = musclesOf(CALVES),
            secondary = musclesOf(QUADS),
            steps = listOf(
                "Apoie as mãos numa parede e dê passo à frente.",
                "Projete o joelho sobre os dedos do pé sem levantar o calcanhar.",
                "Volte e repita em ritmo controlado."
            ),
            videoSearch = "ankle dorsiflexion mobility drill",
            scienceTip = "Boa dorsiflexão auxilia profundidade de agachamento com técnica segura."
        ),
        ex(
            id = "couch_stretch",
            name = "Couch stretch",
            description = "Alongamento para quadríceps e flexores de quadril.",
            level = BEGINNER,
            movementPattern = MP_MOBILITY,
            focus = focusOf(FA_MOBILITY, LEGS),
            equipment = setOf(BENCH),
            primary = musclesOf(QUADS, HIP_FLEXORS),
            secondary = musclesOf(CORE),
            steps = listOf(
                "Apoie um joelho próximo ao sofá/parede.",
                "Mantenha tronco ereto e quadril em extensão.",
                "Sustente respiração lenta por 30-60s por lado."
            ),
            videoSearch = "couch stretch hip flexor",
            scienceTip = "Mobilidade de flexores pode melhorar postura e mecânica em padrões de perna."
        )
    )

    private fun enrichCatalog(source: List<Exercise>): List<Exercise> {
        val progressionChains = listOf(
            listOf("wall_push_up", "push_up_knees", "incline_push_up", "push_up", "tempo_push_up", "diamond_push_up", "archer_push_up", "weighted_push_up_vest"),
            listOf("dead_hang", "active_hang", "scapular_pull_up", "negative_pull_up", "assisted_pull_up", "pull_up", "chest_to_bar_pull_up", "weighted_pull_up_vest", "muscle_up_strict"),
            listOf("bodyweight_squat", "air_squat_pause", "tempo_squat", "split_squat", "shrimp_squat_assisted", "shrimp_squat", "pistol_squat_box"),
            listOf("plank", "hollow_hold", "hollow_rock", "tuck_l_sit", "l_sit", "dragon_flag_tuck", "dragon_flag_negative"),
            listOf("australian_pull_up", "feet_elevated_inverted_row", "ring_row", "archer_pull_up", "typewriter_pull_up"),
            listOf("handstand_wall_hold", "wall_walk", "pike_push_up", "wall_handstand_push_up"),
            listOf("planche_lean_hold", "pseudo_planche_push_up", "tuck_planche_hold"),
            listOf("front_lever_tuck", "front_lever_one_leg")
        )

        val regressions = mutableMapOf<String, MutableSet<String>>()
        val progressions = mutableMapOf<String, MutableSet<String>>()

        progressionChains.forEach { chain ->
            chain.forEachIndexed { index, id ->
                if (index > 0) regressions.getOrPut(id) { linkedSetOf() }.add(chain[index - 1])
                if (index < chain.lastIndex) progressions.getOrPut(id) { linkedSetOf() }.add(chain[index + 1])
            }
        }

        return source
            .distinctBy { it.id }
            .map { exercise ->
                val fallbackReferences = if (exercise.focusAreas.contains(FocusArea.FULL_BODY)) {
                    ScienceReferences.foundational + ScienceReferences.conditioning
                } else {
                    defaultReferencesFor(exercise.focusAreas, exercise.movementPattern)
                }
                val evidence = inferEvidenceLevel(exercise)
                exercise.copy(
                    evidenceLevel = if (exercise.evidenceLevel == MODERATE) evidence else exercise.evidenceLevel,
                    scienceReferences = if (exercise.scienceReferences.isEmpty()) fallbackReferences.distinct() else exercise.scienceReferences,
                    regressions = if (exercise.regressions.isEmpty()) regressions[exercise.id]?.toList().orEmpty() else exercise.regressions,
                    progressions = if (exercise.progressions.isEmpty()) progressions[exercise.id]?.toList().orEmpty() else exercise.progressions
                )
            }
            .sortedBy { it.name }
    }

    private fun inferEvidenceLevel(exercise: Exercise): EvidenceLevel {
        return when {
            exercise.movementPattern == SKILL_STATIC -> APPLIED
            exercise.focusAreas.contains(FocusArea.MOBILITY) -> APPLIED
            exercise.level == ADVANCED -> MODERATE
            else -> HIGH
        }
    }

    private fun defaultReferencesFor(
        focus: Set<FocusArea>,
        movementPattern: MovementPattern
    ): List<String> {
        return when {
            focus.contains(ABS) || movementPattern in setOf(CORE_ANTI_EXTENSION, CORE_ANTI_ROTATION, CORE_FLEXION) -> {
                ScienceReferences.foundational + ScienceReferences.injuryPrevention
            }
            focus.contains(LEGS) -> {
                ScienceReferences.strength + ScienceReferences.injuryPrevention
            }
            focus.contains(BACK) || focus.contains(CHEST) || focus.contains(ARMS) -> {
                ScienceReferences.hypertrophy + ScienceReferences.strength
            }
            focus.contains(FocusArea.MOBILITY) -> {
                ScienceReferences.injuryPrevention + ScienceReferences.foundational
            }
            else -> {
                ScienceReferences.foundational + ScienceReferences.conditioning
            }
        }
    }

    private fun ex(
        id: String,
        name: String,
        description: String,
        level: ExperienceLevel,
        focus: Set<FocusArea>,
        equipment: Set<EquipmentType>,
        primary: Set<MuscleGroup>,
        secondary: Set<MuscleGroup>,
        steps: List<String>,
        videoSearch: String,
        scienceTip: String,
        movementPattern: MovementPattern = inferMovementPattern(focus, primary),
        evidenceLevel: EvidenceLevel = MODERATE,
        scienceReferences: List<String> = emptyList(),
        regressions: List<String> = emptyList(),
        progressions: List<String> = emptyList()
    ): Exercise {
        val media = CuratedExerciseMedia.resolve(
            exerciseId = id,
            movementPattern = movementPattern,
            focusAreas = focus,
            fallbackSearchQuery = videoSearch
        )
        return Exercise(
            id = id,
            name = name,
            description = description,
            level = level,
            movementPattern = movementPattern,
            focusAreas = focus,
            equipment = equipment,
            musclesPrimary = primary,
            musclesSecondary = secondary,
            steps = steps,
            homeImageHint = "Treino em casa: $name",
            homeImageUrl = media.imageUrl,
            videoUrl = media.videoUrl,
            videoEmbedUrl = media.videoEmbedUrl,
            scienceTip = scienceTip,
            evidenceLevel = evidenceLevel,
            scienceReferences = scienceReferences,
            regressions = regressions,
            progressions = progressions
        )
    }

    private fun inferMovementPattern(
        focus: Set<FocusArea>,
        primary: Set<MuscleGroup>
    ): MovementPattern {
        return when {
            focus.contains(FocusArea.MOBILITY) -> MovementPattern.MOBILITY
            focus.contains(ABS) && primary.contains(OBLIQUES) -> CORE_ANTI_ROTATION
            focus.contains(ABS) -> CORE_ANTI_EXTENSION
            focus.contains(LEGS) && primary.contains(HAMSTRINGS) -> HINGE
            focus.contains(LEGS) -> SQUAT
            focus.contains(BACK) && primary.contains(LATS) -> VERTICAL_PULL
            focus.contains(BACK) -> HORIZONTAL_PULL
            focus.contains(CHEST) && primary.contains(SHOULDERS) -> VERTICAL_PUSH
            focus.contains(CHEST) -> HORIZONTAL_PUSH
            focus.contains(FULL_BODY) -> CONDITIONING
            else -> MIXED
        }
    }
}
