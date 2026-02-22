# Treino em Casa Pro (Android)

App Android completo para treino em casa, com foco em calistenia baseada em ciência.

## O que o app entrega

- Enciclopédia de exercícios (109 exercícios cadastrados).
- Calistenia + variações com peso/equipamento.
- Imagem curada por exercício (acervo CDN curado com fallback visual).
- Vídeo embutido por exercício (player via WebView + link externo).
- Área de músculos ativados (mapa frontal/posterior).
- Progressões e regressões por habilidade.
- Plano de 28 dias com progressão semanal.
- Desafios de consistência, streak e histórico de conclusão.
- Histórico detalhado de sessões (séries, reps, carga e RPE) com gráfico semanal.
- Login, conta convidada e backup em nuvem.

## Base científica aplicada

O app usa princípios de:
- Progressão de sobrecarga.
- Distribuição de volume por semana.
- Ajuste de descanso e repetições por objetivo.
- Recuperação ativa e controle de fadiga.
- Progressões técnicas por complexidade de movimento.

Cada exercício possui:
- Dica científica prática.
- Nível de evidência (`Alta`, `Moderada`, `Aplicada`).
- Referências científicas exibidas na tela de detalhe.

Referências centrais estão em:
- `app/src/main/java/com/maicon/treinoemcasa/domain/ScienceReferences.kt`

## Funcionalidades por tela

1. Início
- Resumo de perfil, consistência e status de sincronização.
- Geração de plano de 28 dias.

2. Perfil
- Altura, peso, tipo corporal, objetivo, estilo, nível e equipamentos.
- Login (email/senha), criação de conta e sessão convidada.
- Botões de backup e restauração em nuvem.

3. Exercícios
- Busca e filtros por foco, nível e equipamento.
- Card com imagem, foco, evidência e padrão de movimento.
- Tela detalhada com:
  - execução passo a passo,
  - vídeo embutido,
  - músculos ativados,
  - progressões/regressões,
  - referências científicas.

4. Plano 28D
- 4 semanas com progressão de intensidade/volume.
- Treinos e dias de recuperação ativa.

5. Desafios
- Streak por dia.
- Total de treinos concluídos.
- Metas Bronze/Prata/Ouro/Elite 28D.
- Registro detalhado de sessão por exercício.
- Evolução semanal de volume em gráfico.

## Stack técnica

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- DataStore Preferences
- Firebase Auth + Cloud Firestore
- Coil Compose (imagens)
- Arquitetura ViewModel + estado único do app

## Setup de nuvem (Firebase)

O app funciona offline sem Firebase, mas para login e backup em nuvem é preciso configurar as chaves.

Adicione no `~/.gradle/gradle.properties` (ou `gradle.properties` local):

```properties
FIREBASE_API_KEY=...
FIREBASE_APP_ID=...
FIREBASE_PROJECT_ID=...
FIREBASE_STORAGE_BUCKET=...
FIREBASE_MESSAGING_SENDER_ID=...
```

Depois sincronize o projeto no Android Studio.

## Como rodar

1. Abra a pasta no Android Studio.
2. Aguarde sync do Gradle.
3. Rode em emulador/dispositivo Android (API 26+).

## Estrutura principal

- `app/src/main/java/com/maicon/treinoemcasa/domain`: modelos, referências e gerador científico.
- `app/src/main/java/com/maicon/treinoemcasa/data`: catálogo expandido, persistência local e sync de nuvem.
- `app/src/main/java/com/maicon/treinoemcasa/features`: telas do app.
- `app/src/main/java/com/maicon/treinoemcasa/TreinoApp.kt`: navegação principal.

## Testes

- `app/src/test/java/com/maicon/treinoemcasa/SciencePlanGeneratorTest.kt`
