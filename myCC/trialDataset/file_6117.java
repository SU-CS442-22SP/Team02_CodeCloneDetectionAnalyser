    public void importarHistoricoDeProventos(File pArquivoXLS, boolean pFiltrarPelaDataDeCorteDoCabecalho, Andamento pAndamento) throws IOException, SQLException, InvalidFormatException {
        int iLinha = -1;
        String nomeDaColuna = "";
        Statement stmtLimpezaInicialDestino = null;
        OraclePreparedStatement stmtDestino = null;
        try {
            Workbook arquivo = WorkbookFactory.create(new FileInputStream(pArquivoXLS));
            Sheet plan1 = arquivo.getSheetAt(0);
            int QUANTIDADE_underscoreDE_underscoreREGISTROS_underscoreDE_underscoreMETADADOS = 2;
            int quantidadeDeRegistrosEstimada = plan1.getPhysicalNumberOfRows() - QUANTIDADE_underscoreDE_underscoreREGISTROS_underscoreDE_underscoreMETADADOS;
            String vNomeDePregao, vTipoDaAcao, vDataDaAprovacao, vTipoDoProvento, vDataDoUltimoPrecoCom;
            BigDecimal vValorDoProvento, vUltimoPrecoCom, vProventoPorPreco;
            int vProventoPor1Ou1000Acoes, vPrecoPor1Ou1000Acoes;
            java.sql.Date vUltimoDiaCom;
            DateFormat formatadorData = new SimpleDateFormat("yyyyMMdd");
            DateFormat formatadorPadraoData = DateFormat.getDateInstance();
            Row registro;
            Cell celula;
            java.util.Date dataLimite = plan1.getRow(0).getCell(CampoDaPlanilhaDosProventosEmDinheiro.NOME_underscoreDE_underscorePREGAO.ordinal()).getDateCellValue();
            Cell celulaUltimoDiaCom;
            java.util.Date tmpUltimoDiaCom;
            stmtLimpezaInicialDestino = conDestino.createStatement();
            String sql = "TRUNCATE TABLE TMP_underscoreTB_underscorePROVENTO_underscoreEM_underscoreDINHEIRO";
            stmtLimpezaInicialDestino.executeUpdate(sql);
            sql = "INSERT INTO TMP_underscoreTB_underscorePROVENTO_underscoreEM_underscoreDINHEIRO(NOME_underscoreDE_underscorePREGAO, TIPO_underscoreDA_underscoreACAO, DATA_underscoreDA_underscoreAPROVACAO, VALOR_underscoreDO_underscorePROVENTO, PROVENTO_underscorePOR_underscore1_underscoreOU_underscore1000_underscoreACOES, TIPO_underscoreDO_underscorePROVENTO, ULTIMO_underscoreDIA_underscoreCOM, DATA_underscoreDO_underscoreULTIMO_underscorePRECO_underscoreCOM, ULTIMO_underscorePRECO_underscoreCOM, PRECO_underscorePOR_underscore1_underscoreOU_underscore1000_underscoreACOES, PERC_underscorePROVENTO_underscorePOR_underscorePRECO) VALUES(:NOME_underscoreDE_underscorePREGAO, :TIPO_underscoreDA_underscoreACAO, :DATA_underscoreDA_underscoreAPROVACAO, :VALOR_underscoreDO_underscorePROVENTO, :PROVENTO_underscorePOR_underscore1_underscoreOU_underscore1000_underscoreACOES, :TIPO_underscoreDO_underscorePROVENTO, :ULTIMO_underscoreDIA_underscoreCOM, :DATA_underscoreDO_underscoreULTIMO_underscorePRECO_underscoreCOM, :ULTIMO_underscorePRECO_underscoreCOM, :PRECO_underscorePOR_underscore1_underscoreOU_underscore1000_underscoreACOES, :PERC_underscorePROVENTO_underscorePOR_underscorePRECO)";
            stmtDestino = (OraclePreparedStatement) conDestino.prepareStatement(sql);
            stmtDestino.setExecuteBatch(COMANDOS_underscorePOR_underscoreLOTE);
            int quantidadeDeRegistrosImportados = 0;
            final int NUMERO_underscoreDA_underscoreLINHA_underscoreINICIAL = 1;
            for (iLinha = NUMERO_underscoreDA_underscoreLINHA_underscoreINICIAL; true; iLinha++) {
                registro = plan1.getRow(iLinha);
                if (registro != null) {
                    nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.ULTIMO_underscoreDIA_underscoreCOM.toString();
                    celulaUltimoDiaCom = registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.ULTIMO_underscoreDIA_underscoreCOM.ordinal());
                    if (celulaUltimoDiaCom != null) {
                        if (celulaUltimoDiaCom.getCellType() == Cell.CELL_underscoreTYPE_underscoreNUMERIC) {
                            tmpUltimoDiaCom = celulaUltimoDiaCom.getDateCellValue();
                            if (tmpUltimoDiaCom.compareTo(dataLimite) <= 0 || !pFiltrarPelaDataDeCorteDoCabecalho) {
                                vUltimoDiaCom = new java.sql.Date(celulaUltimoDiaCom.getDateCellValue().getTime());
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.NOME_underscoreDE_underscorePREGAO.toString();
                                vNomeDePregao = registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.NOME_underscoreDE_underscorePREGAO.ordinal()).getStringCellValue().trim();
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.TIPO_underscoreDA_underscoreACAO.toString();
                                vTipoDaAcao = registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.TIPO_underscoreDA_underscoreACAO.ordinal()).getStringCellValue().trim();
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.DATA_underscoreDA_underscoreAPROVACAO.toString();
                                celula = registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.DATA_underscoreDA_underscoreAPROVACAO.ordinal());
                                try {
                                    java.util.Date tmpDataDaAprovacao;
                                    if (celula.getCellType() == Cell.CELL_underscoreTYPE_underscoreNUMERIC) {
                                        tmpDataDaAprovacao = celula.getDateCellValue();
                                    } else {
                                        tmpDataDaAprovacao = formatadorPadraoData.parse(celula.getStringCellValue());
                                    }
                                    vDataDaAprovacao = formatadorData.format(tmpDataDaAprovacao);
                                } catch (ParseException ex) {
                                    vDataDaAprovacao = celula.getStringCellValue();
                                }
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.VALOR_underscoreDO_underscorePROVENTO.toString();
                                vValorDoProvento = new BigDecimal(String.valueOf(registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.VALOR_underscoreDO_underscorePROVENTO.ordinal()).getNumericCellValue()));
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.PROVENTO_underscorePOR_underscore1_underscoreOU_underscore1000_underscoreACOES.toString();
                                vProventoPor1Ou1000Acoes = (int) registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.PROVENTO_underscorePOR_underscore1_underscoreOU_underscore1000_underscoreACOES.ordinal()).getNumericCellValue();
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.TIPO_underscoreDO_underscorePROVENTO.toString();
                                vTipoDoProvento = registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.TIPO_underscoreDO_underscorePROVENTO.ordinal()).getStringCellValue().trim();
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.DATA_underscoreDO_underscoreULTIMO_underscorePRECO_underscoreCOM.toString();
                                celula = registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.DATA_underscoreDO_underscoreULTIMO_underscorePRECO_underscoreCOM.ordinal());
                                if (celula != null) {
                                    try {
                                        java.util.Date tmpDataDoUltimoPrecoCom;
                                        if (celula.getCellType() == Cell.CELL_underscoreTYPE_underscoreNUMERIC) {
                                            tmpDataDoUltimoPrecoCom = celula.getDateCellValue();
                                        } else {
                                            tmpDataDoUltimoPrecoCom = formatadorPadraoData.parse(celula.getStringCellValue());
                                        }
                                        vDataDoUltimoPrecoCom = formatadorData.format(tmpDataDoUltimoPrecoCom);
                                    } catch (ParseException ex) {
                                        vDataDoUltimoPrecoCom = celula.getStringCellValue().trim();
                                    }
                                } else {
                                    vDataDoUltimoPrecoCom = "";
                                }
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.ULTIMO_underscorePRECO_underscoreCOM.toString();
                                vUltimoPrecoCom = new BigDecimal(String.valueOf(registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.ULTIMO_underscorePRECO_underscoreCOM.ordinal()).getNumericCellValue()));
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.PRECO_underscorePOR_underscore1_underscoreOU_underscore1000_underscoreACOES.toString();
                                vPrecoPor1Ou1000Acoes = (int) registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.PRECO_underscorePOR_underscore1_underscoreOU_underscore1000_underscoreACOES.ordinal()).getNumericCellValue();
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.PROVENTO_underscorePOR_underscorePRECO.toString();
                                celula = registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.PROVENTO_underscorePOR_underscorePRECO.ordinal());
                                if (celula != null && celula.getCellType() == Cell.CELL_underscoreTYPE_underscoreNUMERIC) {
                                    vProventoPorPreco = new BigDecimal(String.valueOf(celula.getNumericCellValue()));
                                } else {
                                    vProventoPorPreco = null;
                                }
                                stmtDestino.clearParameters();
                                stmtDestino.setStringAtName("NOME_underscoreDE_underscorePREGAO", vNomeDePregao);
                                stmtDestino.setStringAtName("TIPO_underscoreDA_underscoreACAO", vTipoDaAcao);
                                stmtDestino.setStringAtName("DATA_underscoreDA_underscoreAPROVACAO", vDataDaAprovacao);
                                stmtDestino.setBigDecimalAtName("VALOR_underscoreDO_underscorePROVENTO", vValorDoProvento);
                                stmtDestino.setIntAtName("PROVENTO_underscorePOR_underscore1_underscoreOU_underscore1000_underscoreACOES", vProventoPor1Ou1000Acoes);
                                stmtDestino.setStringAtName("TIPO_underscoreDO_underscorePROVENTO", vTipoDoProvento);
                                stmtDestino.setDateAtName("ULTIMO_underscoreDIA_underscoreCOM", vUltimoDiaCom);
                                stmtDestino.setStringAtName("DATA_underscoreDO_underscoreULTIMO_underscorePRECO_underscoreCOM", vDataDoUltimoPrecoCom);
                                stmtDestino.setBigDecimalAtName("ULTIMO_underscorePRECO_underscoreCOM", vUltimoPrecoCom);
                                stmtDestino.setIntAtName("PRECO_underscorePOR_underscore1_underscoreOU_underscore1000_underscoreACOES", vPrecoPor1Ou1000Acoes);
                                stmtDestino.setBigDecimalAtName("PERC_underscorePROVENTO_underscorePOR_underscorePRECO", vProventoPorPreco);
                                int contagemDasInsercoes = stmtDestino.executeUpdate();
                                quantidadeDeRegistrosImportados++;
                            }
                        }
                    } else {
                        break;
                    }
                    double percentualCompleto = (double) quantidadeDeRegistrosImportados / quantidadeDeRegistrosEstimada * 100;
                    pAndamento.setPercentualCompleto((int) percentualCompleto);
                } else {
                    break;
                }
            }
            conDestino.commit();
        } catch (Exception ex) {
            conDestino.rollback();
            ProblemaNaImportacaoDeArquivo problemaDetalhado = new ProblemaNaImportacaoDeArquivo();
            problemaDetalhado.nomeDoArquivo = pArquivoXLS.getName();
            problemaDetalhado.linhaProblematicaDoArquivo = iLinha + 1;
            problemaDetalhado.colunaProblematicaDoArquivo = nomeDaColuna;
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

