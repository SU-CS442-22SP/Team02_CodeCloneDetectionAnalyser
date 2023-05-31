    public void importarBancoDeDadosDARI(File pArquivoXLS, Andamento pAndamento) throws IOException, SQLException, InvalidFormatException {
        final String ABA_underscoreVALOR_underscoreDE_underscoreMERCADO = "Valor de Mercado";
        final int COLUNA_underscoreDATA = 1, COLUNA_underscoreANO = 6, COLUNA_underscoreVALOR_underscoreDE_underscoreMERCADO_underscoreDIARIO_underscoreEM_underscoreBILHOES_underscoreDE_underscoreREAIS = 2, COLUNA_underscoreVALOR_underscoreDE_underscoreMERCADO_underscoreDIARIO_underscoreEM_underscoreBILHOES_underscoreDE_underscoreDOLARES = 3, COLUNA_underscoreVALOR_underscoreDE_underscoreMERCADO_underscoreANUAL_underscoreEM_underscoreBILHOES_underscoreDE_underscoreREAIS = 7, COLUNA_underscoreVALOR_underscoreDE_underscoreMERCADO_underscoreANUAL_underscoreEM_underscoreBILHOES_underscoreDE_underscoreDOLARES = 8;
        final BigDecimal BILHAO = new BigDecimal("1000000000");
        int iLinha = -1;
        Statement stmtLimpezaInicialDestino = null;
        OraclePreparedStatement stmtDestino = null;
        try {
            Workbook arquivo = WorkbookFactory.create(new FileInputStream(pArquivoXLS));
            Sheet planilhaValorDeMercado = arquivo.getSheet(ABA_underscoreVALOR_underscoreDE_underscoreMERCADO);
            int QUANTIDADE_underscoreDE_underscoreREGISTROS_underscoreDE_underscoreMETADADOS = 7;
            final Calendar DATA_underscoreINICIAL = Calendar.getInstance();
            DATA_underscoreINICIAL.setTime(planilhaValorDeMercado.getRow(QUANTIDADE_underscoreDE_underscoreREGISTROS_underscoreDE_underscoreMETADADOS).getCell(COLUNA_underscoreDATA).getDateCellValue());
            final int ANO_underscoreDA_underscoreDATA_underscoreINICIAL = DATA_underscoreINICIAL.get(Calendar.YEAR);
            final int ANO_underscoreINICIAL = Integer.parseInt(planilhaValorDeMercado.getRow(QUANTIDADE_underscoreDE_underscoreREGISTROS_underscoreDE_underscoreMETADADOS).getCell(COLUNA_underscoreANO).getStringCellValue());
            final int ANO_underscoreFINAL = Calendar.getInstance().get(Calendar.YEAR);
            Row registro;
            int quantidadeDeRegistrosAnuaisEstimada = (ANO_underscoreFINAL - ANO_underscoreINICIAL + 1), quantidadeDeRegistrosDiariosEstimada = (planilhaValorDeMercado.getPhysicalNumberOfRows() - QUANTIDADE_underscoreDE_underscoreREGISTROS_underscoreDE_underscoreMETADADOS);
            final int quantidadeDeRegistrosEstimada = quantidadeDeRegistrosAnuaisEstimada + quantidadeDeRegistrosDiariosEstimada;
            int vAno;
            BigDecimal vValorDeMercadoEmReais, vValorDeMercadoEmDolares;
            Cell celulaDoAno, celulaDoValorDeMercadoEmReais, celulaDoValorDeMercadoEmDolares;
            stmtLimpezaInicialDestino = conDestino.createStatement();
            String sql = "TRUNCATE TABLE TMP_underscoreTB_underscoreVALOR_underscoreMERCADO_underscoreBOLSA";
            stmtLimpezaInicialDestino.executeUpdate(sql);
            sql = "INSERT INTO TMP_underscoreTB_underscoreVALOR_underscoreMERCADO_underscoreBOLSA(DATA, VALOR_underscoreDE_underscoreMERCADO_underscoreREAL, VALOR_underscoreDE_underscoreMERCADO_underscoreDOLAR) VALUES(:DATA, :VALOR_underscoreDE_underscoreMERCADO_underscoreREAL, :VALOR_underscoreDE_underscoreMERCADO_underscoreDOLAR)";
            stmtDestino = (OraclePreparedStatement) conDestino.prepareStatement(sql);
            stmtDestino.setExecuteBatch(COMANDOS_underscorePOR_underscoreLOTE);
            int quantidadeDeRegistrosImportados = 0;
            Calendar calendario = Calendar.getInstance();
            calendario.clear();
            calendario.set(Calendar.MONTH, Calendar.DECEMBER);
            calendario.set(Calendar.DAY_underscoreOF_underscoreMONTH, 31);
            for (iLinha = QUANTIDADE_underscoreDE_underscoreREGISTROS_underscoreDE_underscoreMETADADOS; true; iLinha++) {
                registro = planilhaValorDeMercado.getRow(iLinha);
                celulaDoAno = registro.getCell(COLUNA_underscoreANO);
                String anoTmp = celulaDoAno.getStringCellValue();
                if (anoTmp != null && anoTmp.length() > 0) {
                    vAno = Integer.parseInt(anoTmp);
                    if (vAno < ANO_underscoreDA_underscoreDATA_underscoreINICIAL) {
                        celulaDoValorDeMercadoEmReais = registro.getCell(COLUNA_underscoreVALOR_underscoreDE_underscoreMERCADO_underscoreANUAL_underscoreEM_underscoreBILHOES_underscoreDE_underscoreREAIS);
                        celulaDoValorDeMercadoEmDolares = registro.getCell(COLUNA_underscoreVALOR_underscoreDE_underscoreMERCADO_underscoreANUAL_underscoreEM_underscoreBILHOES_underscoreDE_underscoreDOLARES);
                    } else {
                        break;
                    }
                    calendario.set(Calendar.YEAR, vAno);
                    java.sql.Date vUltimoDiaDoAno = new java.sql.Date(calendario.getTimeInMillis());
                    vValorDeMercadoEmReais = new BigDecimal(celulaDoValorDeMercadoEmReais.getNumericCellValue()).multiply(BILHAO).setScale(0, RoundingMode.DOWN);
                    vValorDeMercadoEmDolares = new BigDecimal(celulaDoValorDeMercadoEmDolares.getNumericCellValue()).multiply(BILHAO).setScale(0, RoundingMode.DOWN);
                    stmtDestino.clearParameters();
                    stmtDestino.setDateAtName("DATA", vUltimoDiaDoAno);
                    stmtDestino.setBigDecimalAtName("VALOR_underscoreDE_underscoreMERCADO_underscoreREAL", vValorDeMercadoEmReais);
                    stmtDestino.setBigDecimalAtName("VALOR_underscoreDE_underscoreMERCADO_underscoreDOLAR", vValorDeMercadoEmDolares);
                    int contagemDasInsercoes = stmtDestino.executeUpdate();
                    quantidadeDeRegistrosImportados++;
                } else {
                    break;
                }
                double percentualCompleto = (double) quantidadeDeRegistrosImportados / quantidadeDeRegistrosEstimada * 100;
                pAndamento.setPercentualCompleto((int) percentualCompleto);
            }
            java.util.Date dataAnterior = null;
            String dataTmp;
            final DateFormat formatadorDeData_underscoreddMMyyyy = new SimpleDateFormat("dd/MM/yyyy", Constantes.IDIOMA_underscorePORTUGUES_underscoreBRASILEIRO);
            final DateFormat formatadorDeData_underscoreddMMMyyyy = new SimpleDateFormat("dd/MMM/yyyy", Constantes.IDIOMA_underscorePORTUGUES_underscoreBRASILEIRO);
            Cell celulaDaData;
            for (iLinha = QUANTIDADE_underscoreDE_underscoreREGISTROS_underscoreDE_underscoreMETADADOS; true; iLinha++) {
                registro = planilhaValorDeMercado.getRow(iLinha);
                if (registro != null) {
                    celulaDaData = registro.getCell(COLUNA_underscoreDATA);
                    java.util.Date data;
                    if (celulaDaData.getCellType() == Cell.CELL_underscoreTYPE_underscoreNUMERIC) {
                        data = celulaDaData.getDateCellValue();
                    } else {
                        dataTmp = celulaDaData.getStringCellValue();
                        try {
                            data = formatadorDeData_underscoreddMMyyyy.parse(dataTmp);
                        } catch (ParseException ex) {
                            data = formatadorDeData_underscoreddMMMyyyy.parse(dataTmp);
                        }
                    }
                    if (dataAnterior == null || data.after(dataAnterior)) {
                        celulaDoValorDeMercadoEmReais = registro.getCell(COLUNA_underscoreVALOR_underscoreDE_underscoreMERCADO_underscoreDIARIO_underscoreEM_underscoreBILHOES_underscoreDE_underscoreREAIS);
                        celulaDoValorDeMercadoEmDolares = registro.getCell(COLUNA_underscoreVALOR_underscoreDE_underscoreMERCADO_underscoreDIARIO_underscoreEM_underscoreBILHOES_underscoreDE_underscoreDOLARES);
                        java.sql.Date vData = new java.sql.Date(data.getTime());
                        vValorDeMercadoEmReais = new BigDecimal(celulaDoValorDeMercadoEmReais.getNumericCellValue()).multiply(BILHAO).setScale(0, RoundingMode.DOWN);
                        vValorDeMercadoEmDolares = new BigDecimal(celulaDoValorDeMercadoEmDolares.getNumericCellValue()).multiply(BILHAO).setScale(0, RoundingMode.DOWN);
                        stmtDestino.clearParameters();
                        stmtDestino.setDateAtName("DATA", vData);
                        stmtDestino.setBigDecimalAtName("VALOR_underscoreDE_underscoreMERCADO_underscoreREAL", vValorDeMercadoEmReais);
                        stmtDestino.setBigDecimalAtName("VALOR_underscoreDE_underscoreMERCADO_underscoreDOLAR", vValorDeMercadoEmDolares);
                        int contagemDasInsercoes = stmtDestino.executeUpdate();
                        quantidadeDeRegistrosImportados++;
                        double percentualCompleto = (double) quantidadeDeRegistrosImportados / quantidadeDeRegistrosEstimada * 100;
                        pAndamento.setPercentualCompleto((int) percentualCompleto);
                    }
                    dataAnterior = data;
                } else {
                    break;
                }
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

