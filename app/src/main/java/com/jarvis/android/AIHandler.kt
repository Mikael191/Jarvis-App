package com.jarvis.android

/**
 * Interface defining the contract for AI interactions.
 * This allows swapping between different providers (Groq, OpenAI, Local LLM) easily.
 */
interface AIHandler {
    suspend fun getResponse(input: String): String
}

/**
 * A Mock implementation of AIHandler for the MVP.
 * This simulates a smart assistant without needing a live API key initially.
 */
class MockAIHandler : AIHandler {
    override suspend fun getResponse(input: String): String {
        // Simulating network delay
        kotlinx.coroutines.delay(500)

        val lowerInput = input.lowercase()
        return when {
            lowerInput.contains("quem é você") || lowerInput.contains("quem e voce") ->
                "Eu sou o Jarvis, seu assistente virtual."
            lowerInput.contains("ola") || lowerInput.contains("olá") ->
                "Olá, senhor. Sistemas prontos."
            lowerInput.contains("criar") && lowerInput.contains("plano") ->
                "Iniciando protocolo de criação de plano. Qual o objetivo?"
            else ->
                "Interessante. Pode me contar mais sobre isso?"
        }
    }
}
