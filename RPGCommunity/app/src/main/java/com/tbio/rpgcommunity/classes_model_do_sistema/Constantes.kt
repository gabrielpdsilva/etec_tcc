package com.tbio.rpgcommunity.classes_model_do_sistema

object Tags{
    const val TAG_ERROR_CPA = "Activity:CPA:Error"
}

object Codigos{
    const val CODIGO_PARA_CADASTRAR_USUARIO = 100
    const val CODIGO_PARA_REQUISITAR_IMAGEM = 108
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
    const val USUARIO_EMAIL_VAZIO = "por favor, preencha o campo email para continuar"
    const val USUARIO_EMAIL_INCORRETO = "por favor, preencha o campo email corretamente para continuar"
    const val USUARIO_CONFS_INVALIDA = "a confirmação da senha preenchida está incorreta"
    const val USUARIO_SENHA_VAZIA = "por favor, preencha o campo senha"
    const val USUARIO_NICKNAME_VAZIO = "por favor, preencha o campo nickname para continuar"

    // divergências nos dados da Sessão
    const val SESSAO_NOME_VAZIO = "por favor, informe o nome da sessão"
}