    public void importarEmissoresDosTitulosFinanceiros(File pArquivoTXT, Andamento pAndamento) throws FileNotFoundException, SQLException {
        int numeroDoRegistro = -1;
        Scanner in = null;
        Statement stmtLimpezaInicialDestino = conDestino.createStatement();
        String sql = "TRUNCATE TABLE TMP_underscoreTB_underscoreEMISSOR_underscoreTITULO";
        stmtLimpezaInicialDestino.executeUpdate(sql);
        sql = "INSERT INTO TMP_underscoreTB_underscoreEMISSOR_underscoreTITULO(SIGLA, NOME, CNPJ, DATA_underscoreCRIACAO) VALUES(:SIGLA, :NOME, :CNPJ, :DATA_underscoreCRIACAO)";
        OraclePreparedStatement stmtDestino = (OraclePreparedStatement) conDestino.prepareStatement(sql);
        stmtDestino.setExecuteBatch(COMANDOS_underscorePOR_underscoreLOTE);
        final int TAMANHO_underscoreDO_underscoreCABECALHO_underscoreDO_underscoreARQUIVO = 0;
        final int TAMANHO_underscoreDO_underscoreRODAPE_underscoreDO_underscoreARQUIVO = 0;
        final int TAMANHO_underscoreDOS_underscoreMETADADOS_underscoreDO_underscoreARQUIVO = TAMANHO_underscoreDO_underscoreCABECALHO_underscoreDO_underscoreARQUIVO + TAMANHO_underscoreDO_underscoreRODAPE_underscoreDO_underscoreARQUIVO;
        final int TAMANHO_underscoreMEDIO_underscorePOR_underscoreREGISTRO = 81;
        long tamanhoDosArquivos = pArquivoTXT.length();
        int quantidadeDeRegistrosEstimada = (int) (tamanhoDosArquivos - TAMANHO_underscoreDOS_underscoreMETADADOS_underscoreDO_underscoreARQUIVO) / TAMANHO_underscoreMEDIO_underscorePOR_underscoreREGISTRO;
        String registro;
        String[] campos;
        try {
            in = new Scanner(new FileInputStream(pArquivoTXT), Constantes.CONJUNTO_underscoreDE_underscoreCARACTERES_underscoreDOS_underscoreARQUIVOS_underscoreTEXTO_underscoreDA_underscoreBOVESPA.name());
            int quantidadeDeRegistrosImportada = 0;
            numeroDoRegistro = 0;
            String vSIGLA, vNOME;
            BigDecimal vCNPJ;
            java.sql.Date vDATA_underscoreCRIACAO;
            final int QTDE_underscoreCAMPOS = CampoDoArquivoDosEmissoresDeTitulosFinanceiros.values().length;
            final String SEPARADOR_underscoreDE_underscoreCAMPOS_underscoreDO_underscoreREGISTRO = ",";
            final String DELIMITADOR_underscoreDE_underscoreCAMPOS_underscoreDO_underscoreREGISTRO = "\"";
            while (in.hasNextLine()) {
                ++numeroDoRegistro;
                registro = in.nextLine();
                stmtDestino.clearParameters();
                registro = registro.substring(1, registro.length() - 1);
                if (registro.endsWith(DELIMITADOR_underscoreDE_underscoreCAMPOS_underscoreDO_underscoreREGISTRO)) {
                    registro = registro + " ";
                }
                campos = registro.split(DELIMITADOR_underscoreDE_underscoreCAMPOS_underscoreDO_underscoreREGISTRO + SEPARADOR_underscoreDE_underscoreCAMPOS_underscoreDO_underscoreREGISTRO + DELIMITADOR_underscoreDE_underscoreCAMPOS_underscoreDO_underscoreREGISTRO);
                int quantidadeDeCamposEncontradosIncluindoOsVazios = campos.length;
                if (quantidadeDeCamposEncontradosIncluindoOsVazios != QTDE_underscoreCAMPOS) {
                    throw new CampoMalDelimitadoEmRegistroDoArquivoImportado(registro);
                }
                vSIGLA = campos[CampoDoArquivoDosEmissoresDeTitulosFinanceiros.SIGLA.ordinal()];
                vNOME = campos[CampoDoArquivoDosEmissoresDeTitulosFinanceiros.NOME.ordinal()];
                String cnpjTmp = campos[CampoDoArquivoDosEmissoresDeTitulosFinanceiros.CNPJ.ordinal()];
                if (cnpjTmp != null && cnpjTmp.trim().length() > 0) {
                    vCNPJ = new BigDecimal(cnpjTmp);
                } else {
                    vCNPJ = null;
                }
                String dataDaCriacaoTmp = campos[CampoDoArquivoDosEmissoresDeTitulosFinanceiros.DATA_underscoreCRIACAO.ordinal()];
                if (dataDaCriacaoTmp != null && dataDaCriacaoTmp.trim().length() > 0) {
                    int dia = Integer.parseInt(dataDaCriacaoTmp.substring(6, 8)), mes = Integer.parseInt(dataDaCriacaoTmp.substring(4, 6)) - 1, ano = Integer.parseInt(dataDaCriacaoTmp.substring(0, 4));
                    Calendar calendario = Calendar.getInstance();
                    calendario.clear();
                    calendario.set(ano, mes, dia);
                    vDATA_underscoreCRIACAO = new java.sql.Date(calendario.getTimeInMillis());
                } else {
                    vDATA_underscoreCRIACAO = null;
                }
                stmtDestino.setStringAtName("SIGLA", vSIGLA);
                stmtDestino.setStringAtName("NOME", vNOME);
                stmtDestino.setBigDecimalAtName("CNPJ", vCNPJ);
                stmtDestino.setDateAtName("DATA_underscoreCRIACAO", vDATA_underscoreCRIACAO);
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

