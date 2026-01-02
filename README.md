# JARVIS Android Assistant

Um assistente virtual futurista para Android, inspirado no J.A.R.V.I.S., construído com arquitetura modular.

## 🚀 Como Iniciar

Este projeto foi gerado como código-fonte completo. Siga os passos abaixo para compilar e rodar no seu dispositivo Android.

### Pré-requisitos
- Android Studio Iguana ou superior.
- JDK 17.

### Instalação

1. **Baixe/Clone** este repositório.
2. Abra o **Android Studio**.
3. Selecione **Open** e navegue até a pasta raiz deste projeto (`JarvisAndroid`).
4. Aguarde o Gradle sincronizar as dependências.
5. Conecte seu dispositivo Android (Modo Desenvolvedor ativado) ou inicie um Emulador.
6. Clique no botão **Run** (Play verde).

## 🧠 Arquitetura

O projeto segue uma arquitetura modular simples:

- **`MainActivity`**: Gerencia a UI e o fluxo principal.
- **`VoiceService`**: Encapsula `SpeechRecognizer` (Ouvido) e `TextToSpeech` (Voz).
- **`AIHandler`**: Interface para o cérebro. Atualmente usa `MockAIHandler` (simulado). Para produção, implemente a chamada para Groq/OpenAI aqui.
- **`CommandHandler`**: Processa comandos locais (ex: bateria, horas) antes de enviar para a IA.
- **`MemoryManager`**: Mantém o contexto da conversa.

## 🛠️ Configuração da IA (Futuro)

Para tornar o Jarvis realmente inteligente:
1. Obtenha uma API Key da Groq, OpenAI ou Anthropic.
2. Modifique `AIHandler.kt` para usar Retrofit e chamar a API.
3. Substitua `MockAIHandler` pela sua implementação real em `MainActivity.kt`.

## 📱 Funcionalidades Atuais (MVP)
- Interface Futurista (HUD Style).
- Botão "Toque para Falar".
- Reconhecimento de Voz (Português).
- Resposta por Voz (TTS Nativo).
- Comandos Básicos simulados.
- Modo de bate-papo (Simulado).

## 🔧 Solução de Problemas (Troubleshooting)

### Erro: "Corrupted Cache" ou "Incompatible Java"
Se você encontrar erros como `org.gradle.cache.internal.btree.CorruptedCacheException` ou incompatibilidade com Java 21:

1. **Java 21**: O projeto foi atualizado para Gradle 8.7, que suporta Java 21. Certifique-se de sincronizar o projeto.
2. **Cache Corrompido**: Se o erro persistir, execute o script de limpeza incluído na raiz do projeto:
   - **Windows**: Dê um duplo clique em `fix_gradle_cache.bat`.
   - **Linux/Mac**: Execute `./fix_gradle_cache.sh` no terminal.
3. Reinicie o Android Studio e tente novamente.

## 📄 Licença
Projeto Open Source.
