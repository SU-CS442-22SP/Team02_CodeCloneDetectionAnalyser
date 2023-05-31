    public void importarEmpresasAbertas(File pArquivoTXT, Andamento pAndamento) throws FileNotFoundException, SQLException {
        int numeroDoRegistro = -1;
        Scanner in = null;
        Statement stmtLimpezaInicialDestino = conDestino.createStatement();
        String sql = "TRUNCATE TABLE TMP_underscoreTB_underscoreCIA_underscoreABERTA";
        stmtLimpezaInicialDestino.executeUpdate(sql);
        sql = "INSERT INTO TMP_underscoreTB_underscoreCIA_underscoreABERTA(CODIGO_underscoreCVM, DENOMINACAO_underscoreSOCIAL, DENOMINACAO_underscoreCOMERCIAL, LOGRADOURO, COMPLEMENTO, BAIRRO, CEP, MUNICIPIO, UF, DDD, TELEFONE, FAX, DENOMINACAO_underscoreANTERIOR, SETOR_underscoreATIVIDADE, CNPJ, DRI, AUDITOR, QUANT_underscoreDE_underscoreACOES_underscoreORDINARIAS, QUANT_underscoreDE_underscoreACOES_underscorePREF, SITUACAO, DATA_underscoreDA_underscoreSITUACAO, TIPO_underscorePAPEL1, TIPO_underscorePAPEL2, TIPO_underscorePAPEL3, TIPO_underscorePAPEL4, TIPO_underscorePAPEL5, TIPO_underscorePAPEL6, CONTROLE_underscoreACIONARIO, DATA_underscoreDE_underscoreREGISTRO, DATA_underscoreDO_underscoreCANCELAMENTO, MERCADO, BOLSA1, BOLSA2, BOLSA3, BOLSA4, BOLSA5, BOLSA6, BOLSA7, BOLSA8, BOLSA9, MOTIVO_underscoreDO_underscoreCANCELAMENTO, PATRIMONIO_underscoreLIQUIDO, DATA_underscoreDO_underscorePATRIMONIO, E_underscoreMAIL, NOME_underscoreSETOR_underscoreATIVIDADE, DATA_underscoreDA_underscoreACAO, TIPO_underscoreNEGOCIO1, TIPO_underscoreNEGOCIO2, TIPO_underscoreNEGOCIO3, TIPO_underscoreNEGOCIO4, TIPO_underscoreNEGOCIO5, TIPO_underscoreNEGOCIO6, TIPO_underscoreMERCADO1, TIPO_underscoreMERCADO2, TIPO_underscoreMERCADO3, TIPO_underscoreMERCADO4, TIPO_underscoreMERCADO5, TIPO_underscoreMERCADO6) VALUES(:CODIGO_underscoreCVM, :DENOMINACAO_underscoreSOCIAL, :DENOMINACAO_underscoreCOMERCIAL, :LOGRADOURO, :COMPLEMENTO, :BAIRRO, :CEP, :MUNICIPIO, :UF, :DDD, :TELEFONE, :FAX, :DENOMINACAO_underscoreANTERIOR, :SETOR_underscoreATIVIDADE, :CNPJ, :DRI, :AUDITOR, :QUANT_underscoreDE_underscoreACOES_underscoreORDINARIAS, :QUANT_underscoreDE_underscoreACOES_underscorePREF, :SITUACAO, :DATA_underscoreDA_underscoreSITUACAO, :TIPO_underscorePAPEL1, :TIPO_underscorePAPEL2, :TIPO_underscorePAPEL3, :TIPO_underscorePAPEL4, :TIPO_underscorePAPEL5, :TIPO_underscorePAPEL6, :CONTROLE_underscoreACIONARIO, :DATA_underscoreDE_underscoreREGISTRO, :DATA_underscoreDO_underscoreCANCELAMENTO, :MERCADO, :BOLSA1, :BOLSA2, :BOLSA3, :BOLSA4, :BOLSA5, :BOLSA6, :BOLSA7, :BOLSA8, :BOLSA9, :MOTIVO_underscoreDO_underscoreCANCELAMENTO, :PATRIMONIO_underscoreLIQUIDO, :DATA_underscoreDO_underscorePATRIMONIO, :E_underscoreMAIL, :NOME_underscoreSETOR_underscoreATIVIDADE, :DATA_underscoreDA_underscoreACAO, :TIPO_underscoreNEGOCIO1, :TIPO_underscoreNEGOCIO2, :TIPO_underscoreNEGOCIO3, :TIPO_underscoreNEGOCIO4, :TIPO_underscoreNEGOCIO5, :TIPO_underscoreNEGOCIO6, :TIPO_underscoreMERCADO1, :TIPO_underscoreMERCADO2, :TIPO_underscoreMERCADO3, :TIPO_underscoreMERCADO4, :TIPO_underscoreMERCADO5, :TIPO_underscoreMERCADO6)";
        OraclePreparedStatement stmtDestino = (OraclePreparedStatement) conDestino.prepareStatement(sql);
        stmtDestino.setExecuteBatch(COMANDOS_underscorePOR_underscoreLOTE);
        final int TAMANHO_underscoreDO_underscoreCABECALHO_underscoreDO_underscoreARQUIVO = 707;
        final int TAMANHO_underscoreDO_underscoreRODAPE_underscoreDO_underscoreARQUIVO = 0;
        final int TAMANHO_underscoreDOS_underscoreMETADADOS_underscoreDO_underscoreARQUIVO = TAMANHO_underscoreDO_underscoreCABECALHO_underscoreDO_underscoreARQUIVO + TAMANHO_underscoreDO_underscoreRODAPE_underscoreDO_underscoreARQUIVO;
        final int TAMANHO_underscoreMEDIO_underscorePOR_underscoreREGISTRO = 659;
        long tamanhoDosArquivos = pArquivoTXT.length();
        int quantidadeDeRegistrosEstimada = (int) (tamanhoDosArquivos - TAMANHO_underscoreDOS_underscoreMETADADOS_underscoreDO_underscoreARQUIVO) / TAMANHO_underscoreMEDIO_underscorePOR_underscoreREGISTRO;
        try {
            in = new Scanner(new FileInputStream(pArquivoTXT), Constantes.CONJUNTO_underscoreDE_underscoreCARACTERES_underscoreDO_underscoreARQUIVO_underscoreTEXTO_underscoreDA_underscoreCVM.name());
            int quantidadeDeRegistrosImportada = 0;
            String registro;
            String[] campos;
            in.nextLine();
            numeroDoRegistro = 0;
            int vCODIGO_underscoreCVM;
            String vDENOMINACAO_underscoreSOCIAL, vDENOMINACAO_underscoreCOMERCIAL, vLOGRADOURO, vCOMPLEMENTO, vBAIRRO;
            BigDecimal vCEP;
            String vMUNICIPIO, vUF;
            BigDecimal vDDD, vTELEFONE, vFAX;
            String vDENOMINACAO_underscoreANTERIOR, vSETOR_underscoreATIVIDADE;
            BigDecimal vCNPJ;
            String vDRI, vAUDITOR;
            BigDecimal vQUANT_underscoreDE_underscoreACOES_underscoreORDINARIAS, vQUANT_underscoreDE_underscoreACOES_underscorePREF;
            String vSITUACAO;
            java.sql.Date vDATA_underscoreDA_underscoreSITUACAO;
            String vTIPO_underscorePAPEL1, vTIPO_underscorePAPEL2, vTIPO_underscorePAPEL3, vTIPO_underscorePAPEL4, vTIPO_underscorePAPEL5, vTIPO_underscorePAPEL6, vCONTROLE_underscoreACIONARIO;
            java.sql.Date vDATA_underscoreDE_underscoreREGISTRO, vDATA_underscoreDO_underscoreCANCELAMENTO;
            String vMERCADO, vBOLSA1, vBOLSA2, vBOLSA3, vBOLSA4, vBOLSA5, vBOLSA6, vBOLSA7, vBOLSA8, vBOLSA9, vMOTIVO_underscoreDO_underscoreCANCELAMENTO;
            BigDecimal vPATRIMONIO_underscoreLIQUIDO;
            java.sql.Date vDATA_underscoreDO_underscorePATRIMONIO;
            String vE_underscoreMAIL, vNOME_underscoreSETOR_underscoreATIVIDADE;
            java.sql.Date vDATA_underscoreDA_underscoreACAO;
            String vTIPO_underscoreNEGOCIO1, vTIPO_underscoreNEGOCIO2, vTIPO_underscoreNEGOCIO3, vTIPO_underscoreNEGOCIO4, vTIPO_underscoreNEGOCIO5, vTIPO_underscoreNEGOCIO6, vTIPO_underscoreMERCADO1, vTIPO_underscoreMERCADO2, vTIPO_underscoreMERCADO3, vTIPO_underscoreMERCADO4, vTIPO_underscoreMERCADO5, vTIPO_underscoreMERCADO6;
            final int QTDE_underscoreCAMPOS = CampoDoArquivoDasEmpresasAbertas.values().length;
            final String SEPARADOR_underscoreDE_underscoreCAMPOS_underscoreDO_underscoreREGISTRO = ";";
            while (in.hasNextLine()) {
                ++numeroDoRegistro;
                registro = in.nextLine();
                stmtDestino.clearParameters();
                ArrayList<String> camposTmp = new ArrayList<String>(QTDE_underscoreCAMPOS);
                StringBuilder campoTmp = new StringBuilder();
                char[] registroTmp = registro.toCharArray();
                char c;
                boolean houveMesclagemDeCampos = false;
                boolean campoIniciaComEspacoEmBranco, campoPossuiConteudo, registroComExcessoDeDelimitadores;
                int quantidadeDeDelimitadoresEncontrados = (registro.length() - registro.replace(SEPARADOR_underscoreDE_underscoreCAMPOS_underscoreDO_underscoreREGISTRO, "").length());
                registroComExcessoDeDelimitadores = (quantidadeDeDelimitadoresEncontrados > (QTDE_underscoreCAMPOS - 1));
                for (int idxCaractere = 0; idxCaractere < registroTmp.length; idxCaractere++) {
                    c = registroTmp[idxCaractere];
                    if (c == SEPARADOR_underscoreDE_underscoreCAMPOS_underscoreDO_underscoreREGISTRO.charAt(0)) {
                        campoPossuiConteudo = (campoTmp.length() > 0 && campoTmp.toString().trim().length() > 0);
                        if (campoPossuiConteudo) {
                            String campoAnterior = null;
                            if (camposTmp.size() > 0) {
                                campoAnterior = camposTmp.get(camposTmp.size() - 1);
                            }
                            campoIniciaComEspacoEmBranco = campoTmp.toString().startsWith(" ");
                            if (campoAnterior != null && campoIniciaComEspacoEmBranco && registroComExcessoDeDelimitadores) {
                                camposTmp.set(camposTmp.size() - 1, (campoAnterior + campoTmp.toString()).trim());
                                houveMesclagemDeCampos = true;
                            } else {
                                camposTmp.add(campoTmp.toString().trim());
                            }
                        } else {
                            camposTmp.add(null);
                        }
                        campoTmp.setLength(0);
                    } else {
                        campoTmp.append(c);
                    }
                }
                if (registro.endsWith(SEPARADOR_underscoreDE_underscoreCAMPOS_underscoreDO_underscoreREGISTRO)) {
                    camposTmp.add(null);
                }
                if (houveMesclagemDeCampos && camposTmp.size() < QTDE_underscoreCAMPOS) {
                    camposTmp.add(CampoDoArquivoDasEmpresasAbertas.COMPLEMENTO.ordinal(), null);
                }
                campos = camposTmp.toArray(new String[camposTmp.size()]);
                int quantidadeDeCamposEncontradosIncluindoOsVazios = campos.length;
                if (quantidadeDeCamposEncontradosIncluindoOsVazios != QTDE_underscoreCAMPOS) {
                    throw new CampoMalDelimitadoEmRegistroDoArquivoImportado(registro);
                }
                vCODIGO_underscoreCVM = Integer.parseInt(campos[CampoDoArquivoDasEmpresasAbertas.CODIGO_underscoreCVM.ordinal()]);
                vDENOMINACAO_underscoreSOCIAL = campos[CampoDoArquivoDasEmpresasAbertas.DENOMINACAO_underscoreSOCIAL.ordinal()];
                vDENOMINACAO_underscoreCOMERCIAL = campos[CampoDoArquivoDasEmpresasAbertas.DENOMINACAO_underscoreCOMERCIAL.ordinal()];
                vLOGRADOURO = campos[CampoDoArquivoDasEmpresasAbertas.LOGRADOURO.ordinal()];
                vCOMPLEMENTO = campos[CampoDoArquivoDasEmpresasAbertas.COMPLEMENTO.ordinal()];
                vBAIRRO = campos[CampoDoArquivoDasEmpresasAbertas.BAIRRO.ordinal()];
                String cepTmp = campos[CampoDoArquivoDasEmpresasAbertas.CEP.ordinal()];
                if (cepTmp != null && cepTmp.trim().length() > 0) {
                    vCEP = new BigDecimal(cepTmp);
                } else {
                    vCEP = null;
                }
                vMUNICIPIO = campos[CampoDoArquivoDasEmpresasAbertas.MUNICIPIO.ordinal()];
                vUF = campos[CampoDoArquivoDasEmpresasAbertas.UF.ordinal()];
                String dddTmp = campos[CampoDoArquivoDasEmpresasAbertas.DDD.ordinal()], foneTmp = campos[CampoDoArquivoDasEmpresasAbertas.TELEFONE.ordinal()], dddFone = "";
                if (dddTmp != null && dddTmp.trim().length() > 0) {
                    dddFone = dddFone + dddTmp;
                }
                if (foneTmp != null && foneTmp.trim().length() > 0) {
                    dddFone = dddFone + foneTmp;
                }
                if (dddFone != null && dddFone.trim().length() > 0) {
                    dddFone = new BigDecimal(dddFone).toString();
                    if (dddFone.length() > 10 && dddFone.endsWith("0")) {
                        dddFone = dddFone.substring(0, 10);
                    }
                    vDDD = new BigDecimal(dddFone.substring(0, 2));
                    vTELEFONE = new BigDecimal(dddFone.substring(2));
                } else {
                    vDDD = null;
                    vTELEFONE = null;
                }
                String faxTmp = campos[CampoDoArquivoDasEmpresasAbertas.FAX.ordinal()];
                if (faxTmp != null && faxTmp.trim().length() > 0) {
                    vFAX = new BigDecimal(faxTmp);
                } else {
                    vFAX = null;
                }
                vDENOMINACAO_underscoreANTERIOR = campos[CampoDoArquivoDasEmpresasAbertas.DENOMINACAO_underscoreANTERIOR.ordinal()];
                vSETOR_underscoreATIVIDADE = campos[CampoDoArquivoDasEmpresasAbertas.SETOR_underscoreATIVIDADE.ordinal()];
                String cnpjTmp = campos[CampoDoArquivoDasEmpresasAbertas.CNPJ.ordinal()];
                if (cnpjTmp != null && cnpjTmp.trim().length() > 0) {
                    vCNPJ = new BigDecimal(cnpjTmp);
                } else {
                    vCNPJ = null;
                }
                vDRI = campos[CampoDoArquivoDasEmpresasAbertas.DRI.ordinal()];
                vAUDITOR = campos[CampoDoArquivoDasEmpresasAbertas.AUDITOR.ordinal()];
                String qtdeAcoesON = campos[CampoDoArquivoDasEmpresasAbertas.QUANT_underscoreDE_underscoreACOES_underscoreORDINARIAS.ordinal()];
                if (qtdeAcoesON != null && qtdeAcoesON.trim().length() > 0) {
                    vQUANT_underscoreDE_underscoreACOES_underscoreORDINARIAS = new BigDecimal(qtdeAcoesON);
                } else {
                    vQUANT_underscoreDE_underscoreACOES_underscoreORDINARIAS = null;
                }
                String qtdeAcoesPN = campos[CampoDoArquivoDasEmpresasAbertas.QUANT_underscoreDE_underscoreACOES_underscorePREF.ordinal()];
                if (qtdeAcoesPN != null && qtdeAcoesPN.trim().length() > 0) {
                    vQUANT_underscoreDE_underscoreACOES_underscorePREF = new BigDecimal(qtdeAcoesPN);
                } else {
                    vQUANT_underscoreDE_underscoreACOES_underscorePREF = null;
                }
                vSITUACAO = campos[CampoDoArquivoDasEmpresasAbertas.SITUACAO.ordinal()];
                String dataDaSituacaoTmp = campos[CampoDoArquivoDasEmpresasAbertas.DATA_underscoreDA_underscoreSITUACAO.ordinal()];
                String[] partesDaData = dataDaSituacaoTmp.trim().split("/");
                int dia = Integer.parseInt(partesDaData[0]), mes = Integer.parseInt(partesDaData[1]) - 1, ano = Integer.parseInt(partesDaData[2]);
                Calendar calendario = Calendar.getInstance();
                calendario.clear();
                calendario.set(ano, mes, dia);
                vDATA_underscoreDA_underscoreSITUACAO = new java.sql.Date(calendario.getTimeInMillis());
                vTIPO_underscorePAPEL1 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscorePAPEL1.ordinal()];
                vTIPO_underscorePAPEL2 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscorePAPEL2.ordinal()];
                vTIPO_underscorePAPEL3 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscorePAPEL3.ordinal()];
                vTIPO_underscorePAPEL4 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscorePAPEL4.ordinal()];
                vTIPO_underscorePAPEL5 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscorePAPEL5.ordinal()];
                vTIPO_underscorePAPEL6 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscorePAPEL6.ordinal()];
                vCONTROLE_underscoreACIONARIO = campos[CampoDoArquivoDasEmpresasAbertas.CONTROLE_underscoreACIONARIO.ordinal()];
                String dataDeRegistroTmp = campos[CampoDoArquivoDasEmpresasAbertas.DATA_underscoreDE_underscoreREGISTRO.ordinal()];
                partesDaData = dataDeRegistroTmp.trim().split("/");
                dia = Integer.parseInt(partesDaData[0]);
                mes = Integer.parseInt(partesDaData[1]) - 1;
                ano = Integer.parseInt(partesDaData[2]);
                calendario = Calendar.getInstance();
                calendario.clear();
                calendario.set(ano, mes, dia);
                vDATA_underscoreDE_underscoreREGISTRO = new java.sql.Date(calendario.getTimeInMillis());
                String dataDoCancelamentoTmp = campos[CampoDoArquivoDasEmpresasAbertas.DATA_underscoreDO_underscoreCANCELAMENTO.ordinal()];
                if (dataDoCancelamentoTmp != null && dataDoCancelamentoTmp.trim().length() > 0) {
                    partesDaData = dataDoCancelamentoTmp.trim().split("/");
                    dia = Integer.parseInt(partesDaData[0]);
                    mes = Integer.parseInt(partesDaData[1]) - 1;
                    ano = Integer.parseInt(partesDaData[2]);
                    calendario = Calendar.getInstance();
                    calendario.clear();
                    calendario.set(ano, mes, dia);
                    vDATA_underscoreDO_underscoreCANCELAMENTO = new java.sql.Date(calendario.getTimeInMillis());
                } else {
                    vDATA_underscoreDO_underscoreCANCELAMENTO = null;
                }
                vMERCADO = campos[CampoDoArquivoDasEmpresasAbertas.MERCADO.ordinal()];
                vBOLSA1 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA1.ordinal()];
                vBOLSA2 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA2.ordinal()];
                vBOLSA3 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA3.ordinal()];
                vBOLSA4 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA4.ordinal()];
                vBOLSA5 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA5.ordinal()];
                vBOLSA6 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA6.ordinal()];
                vBOLSA7 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA7.ordinal()];
                vBOLSA8 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA8.ordinal()];
                vBOLSA9 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA9.ordinal()];
                vMOTIVO_underscoreDO_underscoreCANCELAMENTO = campos[CampoDoArquivoDasEmpresasAbertas.MOTIVO_underscoreDO_underscoreCANCELAMENTO.ordinal()];
                String patrimonioLiquidoTmp = campos[CampoDoArquivoDasEmpresasAbertas.PATRIMONIO_underscoreLIQUIDO.ordinal()];
                if (patrimonioLiquidoTmp != null && patrimonioLiquidoTmp.trim().length() > 0) {
                    vPATRIMONIO_underscoreLIQUIDO = new BigDecimal(patrimonioLiquidoTmp);
                } else {
                    vPATRIMONIO_underscoreLIQUIDO = null;
                }
                String dataDoPatrimonioTmp = campos[CampoDoArquivoDasEmpresasAbertas.DATA_underscoreDO_underscorePATRIMONIO.ordinal()];
                if (dataDoPatrimonioTmp != null && dataDoPatrimonioTmp.trim().length() > 0) {
                    partesDaData = dataDoPatrimonioTmp.trim().split("/");
                    dia = Integer.parseInt(partesDaData[0]);
                    mes = Integer.parseInt(partesDaData[1]) - 1;
                    ano = Integer.parseInt(partesDaData[2]);
                    calendario = Calendar.getInstance();
                    calendario.clear();
                    calendario.set(ano, mes, dia);
                    vDATA_underscoreDO_underscorePATRIMONIO = new java.sql.Date(calendario.getTimeInMillis());
                } else {
                    vDATA_underscoreDO_underscorePATRIMONIO = null;
                }
                vE_underscoreMAIL = campos[CampoDoArquivoDasEmpresasAbertas.E_underscoreMAIL.ordinal()];
                vNOME_underscoreSETOR_underscoreATIVIDADE = campos[CampoDoArquivoDasEmpresasAbertas.NOME_underscoreSETOR_underscoreATIVIDADE.ordinal()];
                String dataDaAcaoTmp = campos[CampoDoArquivoDasEmpresasAbertas.DATA_underscoreDA_underscoreACAO.ordinal()];
                if (dataDaAcaoTmp != null && dataDaAcaoTmp.trim().length() > 0) {
                    partesDaData = dataDaAcaoTmp.trim().split("/");
                    dia = Integer.parseInt(partesDaData[0]);
                    mes = Integer.parseInt(partesDaData[1]) - 1;
                    ano = Integer.parseInt(partesDaData[2]);
                    calendario = Calendar.getInstance();
                    calendario.clear();
                    calendario.set(ano, mes, dia);
                    vDATA_underscoreDA_underscoreACAO = new java.sql.Date(calendario.getTimeInMillis());
                } else {
                    vDATA_underscoreDA_underscoreACAO = null;
                }
                vTIPO_underscoreNEGOCIO1 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscoreNEGOCIO1.ordinal()];
                vTIPO_underscoreNEGOCIO2 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscoreNEGOCIO2.ordinal()];
                vTIPO_underscoreNEGOCIO3 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscoreNEGOCIO3.ordinal()];
                vTIPO_underscoreNEGOCIO4 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscoreNEGOCIO4.ordinal()];
                vTIPO_underscoreNEGOCIO5 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscoreNEGOCIO5.ordinal()];
                vTIPO_underscoreNEGOCIO6 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscoreNEGOCIO6.ordinal()];
                vTIPO_underscoreMERCADO1 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscoreMERCADO1.ordinal()];
                vTIPO_underscoreMERCADO2 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscoreMERCADO2.ordinal()];
                vTIPO_underscoreMERCADO3 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscoreMERCADO3.ordinal()];
                vTIPO_underscoreMERCADO4 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscoreMERCADO4.ordinal()];
                vTIPO_underscoreMERCADO5 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscoreMERCADO5.ordinal()];
                vTIPO_underscoreMERCADO6 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_underscoreMERCADO6.ordinal()];
                stmtDestino.setIntAtName("CODIGO_underscoreCVM", vCODIGO_underscoreCVM);
                stmtDestino.setStringAtName("DENOMINACAO_underscoreSOCIAL", vDENOMINACAO_underscoreSOCIAL);
                stmtDestino.setStringAtName("DENOMINACAO_underscoreCOMERCIAL", vDENOMINACAO_underscoreCOMERCIAL);
                stmtDestino.setStringAtName("LOGRADOURO", vLOGRADOURO);
                stmtDestino.setStringAtName("COMPLEMENTO", vCOMPLEMENTO);
                stmtDestino.setStringAtName("BAIRRO", vBAIRRO);
                stmtDestino.setBigDecimalAtName("CEP", vCEP);
                stmtDestino.setStringAtName("MUNICIPIO", vMUNICIPIO);
                stmtDestino.setStringAtName("UF", vUF);
                stmtDestino.setBigDecimalAtName("DDD", vDDD);
                stmtDestino.setBigDecimalAtName("TELEFONE", vTELEFONE);
                stmtDestino.setBigDecimalAtName("FAX", vFAX);
                stmtDestino.setStringAtName("DENOMINACAO_underscoreANTERIOR", vDENOMINACAO_underscoreANTERIOR);
                stmtDestino.setStringAtName("SETOR_underscoreATIVIDADE", vSETOR_underscoreATIVIDADE);
                stmtDestino.setBigDecimalAtName("CNPJ", vCNPJ);
                stmtDestino.setStringAtName("DRI", vDRI);
                stmtDestino.setStringAtName("AUDITOR", vAUDITOR);
                stmtDestino.setBigDecimalAtName("QUANT_underscoreDE_underscoreACOES_underscoreORDINARIAS", vQUANT_underscoreDE_underscoreACOES_underscoreORDINARIAS);
                stmtDestino.setBigDecimalAtName("QUANT_underscoreDE_underscoreACOES_underscorePREF", vQUANT_underscoreDE_underscoreACOES_underscorePREF);
                stmtDestino.setStringAtName("SITUACAO", vSITUACAO);
                stmtDestino.setDateAtName("DATA_underscoreDA_underscoreSITUACAO", vDATA_underscoreDA_underscoreSITUACAO);
                stmtDestino.setStringAtName("TIPO_underscorePAPEL1", vTIPO_underscorePAPEL1);
                stmtDestino.setStringAtName("TIPO_underscorePAPEL2", vTIPO_underscorePAPEL2);
                stmtDestino.setStringAtName("TIPO_underscorePAPEL3", vTIPO_underscorePAPEL3);
                stmtDestino.setStringAtName("TIPO_underscorePAPEL4", vTIPO_underscorePAPEL4);
                stmtDestino.setStringAtName("TIPO_underscorePAPEL5", vTIPO_underscorePAPEL5);
                stmtDestino.setStringAtName("TIPO_underscorePAPEL6", vTIPO_underscorePAPEL6);
                stmtDestino.setStringAtName("CONTROLE_underscoreACIONARIO", vCONTROLE_underscoreACIONARIO);
                stmtDestino.setDateAtName("DATA_underscoreDE_underscoreREGISTRO", vDATA_underscoreDE_underscoreREGISTRO);
                stmtDestino.setDateAtName("DATA_underscoreDO_underscoreCANCELAMENTO", vDATA_underscoreDO_underscoreCANCELAMENTO);
                stmtDestino.setStringAtName("MERCADO", vMERCADO);
                stmtDestino.setStringAtName("BOLSA1", vBOLSA1);
                stmtDestino.setStringAtName("BOLSA2", vBOLSA2);
                stmtDestino.setStringAtName("BOLSA3", vBOLSA3);
                stmtDestino.setStringAtName("BOLSA4", vBOLSA4);
                stmtDestino.setStringAtName("BOLSA5", vBOLSA5);
                stmtDestino.setStringAtName("BOLSA6", vBOLSA6);
                stmtDestino.setStringAtName("BOLSA7", vBOLSA7);
                stmtDestino.setStringAtName("BOLSA8", vBOLSA8);
                stmtDestino.setStringAtName("BOLSA9", vBOLSA9);
                stmtDestino.setStringAtName("MOTIVO_underscoreDO_underscoreCANCELAMENTO", vMOTIVO_underscoreDO_underscoreCANCELAMENTO);
                stmtDestino.setBigDecimalAtName("PATRIMONIO_underscoreLIQUIDO", vPATRIMONIO_underscoreLIQUIDO);
                stmtDestino.setDateAtName("DATA_underscoreDO_underscorePATRIMONIO", vDATA_underscoreDO_underscorePATRIMONIO);
                stmtDestino.setStringAtName("E_underscoreMAIL", vE_underscoreMAIL);
                stmtDestino.setStringAtName("NOME_underscoreSETOR_underscoreATIVIDADE", vNOME_underscoreSETOR_underscoreATIVIDADE);
                stmtDestino.setDateAtName("DATA_underscoreDA_underscoreACAO", vDATA_underscoreDA_underscoreACAO);
                stmtDestino.setStringAtName("TIPO_underscoreNEGOCIO1", vTIPO_underscoreNEGOCIO1);
                stmtDestino.setStringAtName("TIPO_underscoreNEGOCIO2", vTIPO_underscoreNEGOCIO2);
                stmtDestino.setStringAtName("TIPO_underscoreNEGOCIO3", vTIPO_underscoreNEGOCIO3);
                stmtDestino.setStringAtName("TIPO_underscoreNEGOCIO4", vTIPO_underscoreNEGOCIO4);
                stmtDestino.setStringAtName("TIPO_underscoreNEGOCIO5", vTIPO_underscoreNEGOCIO5);
                stmtDestino.setStringAtName("TIPO_underscoreNEGOCIO6", vTIPO_underscoreNEGOCIO6);
                stmtDestino.setStringAtName("TIPO_underscoreMERCADO1", vTIPO_underscoreMERCADO1);
                stmtDestino.setStringAtName("TIPO_underscoreMERCADO2", vTIPO_underscoreMERCADO2);
                stmtDestino.setStringAtName("TIPO_underscoreMERCADO3", vTIPO_underscoreMERCADO3);
                stmtDestino.setStringAtName("TIPO_underscoreMERCADO4", vTIPO_underscoreMERCADO4);
                stmtDestino.setStringAtName("TIPO_underscoreMERCADO5", vTIPO_underscoreMERCADO5);
                stmtDestino.setStringAtName("TIPO_underscoreMERCADO6", vTIPO_underscoreMERCADO6);
                int contagemDasInsercoes = stmtDestino.executeUpdate();
                quantidadeDeRegistrosImportada++;
                double percentualCompleto = (double) quantidadeDeRegistrosImportada / quantidadeDeRegistrosEstimada * 100;
                pAndamento.setPercentualCompleto((int) percentualCompleto);
            }
            conDestino.commit();
        } catch (Exception ex) {
            conDestino.rollback();
            ProblemaNaImportacaoDeArquivo problemaDetalhado = new ProblemaNaImportacaoDeArquivo();
            problemaDetalhado.nomeDoArquivo = pArquivoTXT.getName();
            problemaDetalhado.linhaProblematicaDoArquivo = numeroDoRegistro;
            problemaDetalhado.detalhesSobreOProblema = ex;
            throw problemaDetalhado;
        } finally {
            pAndamento.setPercentualCompleto(100);
            in.close();
            if (stmtLimpezaInicialDestino != null && (!stmtLimpezaInicialDestino.isClosed())) {
                stmtLimpezaInicialDestino.close();
            }
            if (stmtDestino != null && (!stmtDestino.isClosed())) {
                stmtDestino.close();
            }
        }
    }

