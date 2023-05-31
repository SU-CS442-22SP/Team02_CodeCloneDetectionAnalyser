    public void importarSetor(File pArquivoXLS, String pCabecalhoSetor, Andamento pAndamento) throws FileNotFoundException, IOException, SQLException, InvalidFormatException {
        int iLinha = -1;
        Statement stmtLimpezaInicialDestino = null;
        OraclePreparedStatement stmtDestino = null;
        try {
            Workbook arquivo = WorkbookFactory.create(new FileInputStream(pArquivoXLS));
            Sheet plan1 = arquivo.getSheetAt(0);
            int QUANTIDADE_underscoreDE_underscoreREGISTROS_underscoreDE_underscoreCABECALHO = 7;
            int QUANTIDADE_underscoreDE_underscoreREGISTROS_underscoreDE_underscoreRODAPE = 14;
            int QUANTIDADE_underscoreDE_underscoreREGISTROS_underscoreDE_underscoreMETADADOS = QUANTIDADE_underscoreDE_underscoreREGISTROS_underscoreDE_underscoreCABECALHO + QUANTIDADE_underscoreDE_underscoreREGISTROS_underscoreDE_underscoreRODAPE;
            int quantidadeDeRegistrosEstimada = plan1.getPhysicalNumberOfRows() - QUANTIDADE_underscoreDE_underscoreREGISTROS_underscoreDE_underscoreMETADADOS;
            String vSetor = "", vSubsetor = "", vSegmento = "";
            LinhaDaPlanilhaDosSetores registroAtual;
            int vPapeisPorSegmento = 0;
            stmtLimpezaInicialDestino = conDestino.createStatement();
            String sql = "TRUNCATE TABLE TMP_underscoreTB_underscoreSETOR_underscoreSUBSETOR_underscoreSEGMENTO";
            stmtLimpezaInicialDestino.executeUpdate(sql);
            sql = "INSERT INTO TMP_underscoreTB_underscoreSETOR_underscoreSUBSETOR_underscoreSEGMENTO(SIGLA_underscoreEMPRESA, NOME_underscoreSETOR, NOME_underscoreSUBSETOR, NOME_underscoreSEGMENTO) VALUES(:SIGLA_underscoreEMPRESA, :NOME_underscoreSETOR, :NOME_underscoreSUBSETOR, :NOME_underscoreSEGMENTO)";
            stmtDestino = (OraclePreparedStatement) conDestino.prepareStatement(sql);
            stmtDestino.setExecuteBatch(COMANDOS_underscorePOR_underscoreLOTE);
            int quantidadeDeRegistrosImportados = 0;
            iLinha = 8;
            while (true) {
                registroAtual = new LinhaDaPlanilhaDosSetores(plan1.getRow(iLinha));
                if (registroAtual.nomeDoSetor.length() > 0 && !registroAtual.nomeDoSetor.equalsIgnoreCase(pCabecalhoSetor)) {
                    if (registroAtual.nomeDoSubsetor.equalsIgnoreCase("")) {
                        break;
                    } else {
                        vSetor = registroAtual.nomeDoSetor;
                        vSubsetor = null;
                        vSegmento = null;
                    }
                }
                if (registroAtual.nomeDoSubsetor.length() > 0 && !registroAtual.nomeDoSetor.equalsIgnoreCase(pCabecalhoSetor)) {
                    vSubsetor = registroAtual.nomeDoSubsetor;
                    vSegmento = null;
                }
                String nomeDoSegmento = registroAtual.nomeDoSegmentoOuDaEmpresa;
                if (nomeDoSegmento.length() > 0 && !registroAtual.nomeDoSetor.equalsIgnoreCase(pCabecalhoSetor) && registroAtual.siglaDaEmpresa.equals("")) {
                    if (vSegmento != null && vPapeisPorSegmento == 0) {
                        vSegmento = vSegmento + " " + nomeDoSegmento;
                    } else {
                        vSegmento = nomeDoSegmento;
                    }
                    vPapeisPorSegmento = 0;
                }
                String nomeDaEmpresa = registroAtual.nomeDoSegmentoOuDaEmpresa;
                if (registroAtual.siglaDaEmpresa.length() == 4 && !registroAtual.nomeDoSetor.equalsIgnoreCase(pCabecalhoSetor) && !nomeDaEmpresa.equals("")) {
                    String vCodneg = registroAtual.siglaDaEmpresa;
                    stmtDestino.clearParameters();
                    stmtDestino.setStringAtName("SIGLA_underscoreEMPRESA", vCodneg);
                    stmtDestino.setStringAtName("NOME_underscoreSETOR", vSetor);
                    stmtDestino.setStringAtName("NOME_underscoreSUBSETOR", vSubsetor);
                    stmtDestino.setStringAtName("NOME_underscoreSEGMENTO", vSegmento);
                    int contagemDasInsercoes = stmtDestino.executeUpdate();
                    quantidadeDeRegistrosImportados++;
                    vPapeisPorSegmento++;
                }
                iLinha++;
                double percentualCompleto = (double) quantidadeDeRegistrosImportados / quantidadeDeRegistrosEstimada * 100;
                pAndamento.setPercentualCompleto((int) percentualCompleto);
            }
            conDestino.commit();
        } catch (Exception ex) {
            conDestino.rollback();
            ProblemaNaImportacaoDeArquivo problemaDetalhado = new ProblemaNaImportacaoDeArquivo();
            problemaDetalhado.nomeDoArquivo = pArquivoXLS.getName();
            problemaDetalhado.linhaProblematicaDoArquivo = iLinha;
            problemaDetalhado.detalhesSobreOProblema = ex;
            throw problemaDetalhado;
        } finally {
            pAndamento.setPercentualCompleto(100);
            if (stmtLimpezaInicialDestino != null && (!stmtLimpezaInicialDestino.isClosed())) {
                stmtLimpezaInicialDestino.close();
            }
            if (stmtDestino != null && (!stmtDestino.isClosed())) {
                stmtDestino.close();
            }
        }
    }

