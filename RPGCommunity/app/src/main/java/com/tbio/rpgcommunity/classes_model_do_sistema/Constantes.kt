package com.tbio.rpgcommunity.classes_model_do_sistema

object Tags{
    const val TAG_ERROR_CPA = "Activity:CPA:Error"
}

object Codigos{
    const val CODIGO_PARA_CADASTRAR_USUARIO = 100
    const val CODIGO_PARA_REQUISITAR_IMAGEM = 108
    const val CODIGO_PARA_ADICIONAR_JOGADOR: Int = 116

    const val ACAO = 150
    const val FALA = 154
    const val PENSAMENTO = 158
    const val COMUM = 160
    const val SISTEMA = 162
}

object Palavras {
    const val ACAO = "acao"
    const val FALA = "fala"
    const val PENSAMENTO = "pensamento"
    const val COMUM = "comum"
    const val SISTEMA = "sistema"
}

object Erros{
    const val IAA = "informe ao administrador do aplicativo."

    // erros de usuario
    const val ERRO_AO_CRIAR_USUARIO = "ocorreu um erro no cadastro, " + IAA
    const val ERRO_AO_LOGAR_USUARIO = "ocorreu um erro ao logar o usuario, " + IAA
    const val ERRO_AO_CRIAR_SESSAO = "ocorreu um erro na criação da sessão, " + IAA

    // erros de personagem
    const val ERRO_AO_CRIAR_PERSONAGEM = "ocorreu um erro na criação do personagem, " + IAA
}

object Divergencias{
    // divergências nos dados do usuário
    const val USUARIO_EMAIL_VAZIO = "Por favor, preencha o campo email para continuar."
    const val USUARIO_EMAIL_INCORRETO = "Por favor, preencha o campo email corretamente para continuar."
    const val USUARIO_CONFS_INVALIDA = "A confirmação da senha preenchida está incorreta."
    const val USUARIO_SENHA_VAZIA = "Por favor, preencha o campo senha."
    const val USUARIO_NICKNAME_VAZIO = "Por favor, preencha o campo nickname para continuar."

    // divergências nos dados da Sessão
    const val SESSAO_NOME_VAZIO = "Por favor, informe o nome da sessão."
    const val SESSAO_SISTEMA_VAZIO = "Por favor, informe o sistema da sessão."
    const val SESSAO_DESCRICAO_VAZIA = "Por favor, informe uma descrição para sua sessão."

    //divergências nos dados do personagem
    const val PERSONAGEM_NOME_VAZIO = "Por favor, preencha o nome do personagem."
    const val PERSONAGEM_GENERO_VAZIO = "Por favor, escolha um gênero para o personagem"

    const val RESET_CAMPO_VAZIO = "Por favor, preencha este campo."

}