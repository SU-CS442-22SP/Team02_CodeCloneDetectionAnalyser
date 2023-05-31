    public void importarHistoricoDoPIB(Andamento pAndamento) throws FileNotFoundException, SQLException, Exception {
        pAndamento.delimitarIntervaloDeVariacao(0, 49);
        PIB[] valoresPendentesDoPIB = obterValoresPendentesDoPIB(pAndamento);
        pAndamento.delimitarIntervaloDeVariacao(50, 100);
        if (valoresPendentesDoPIB != null && valoresPendentesDoPIB.length > 0) {
            String sql = "INSERT INTO tmp_underscoreTB_underscorePIB(ULTIMO_underscoreDIA_underscoreDO_underscoreMES, PIB_underscoreACUM_underscore12MESES_underscoreREAL, PIB_underscoreACUM_underscore12MESES_underscoreDOLAR) VALUES(:ULTIMO_underscoreDIA_underscoreDO_underscoreMES, :PIB_underscoreACUM_underscore12MESES_underscoreREAL, :PIB_underscoreACUM_underscore12MESES_underscoreDOLAR)";
            OraclePreparedStatement stmtDestino = (OraclePreparedStatement) conDestino.prepareStatement(sql);
            stmtDestino.setExecuteBatch(COMANDOS_underscorePOR_underscoreLOTE);
            int quantidadeDeRegistrosASeremImportados = valoresPendentesDoPIB.length;
            try {
                int quantidadeDeRegistrosImportados = 0;
                int numeroDoRegistro = 0;
                final BigDecimal MILHAO = new BigDecimal("1000000");
                for (PIB valorPendenteDoPIB : valoresPendentesDoPIB) {
                    ++numeroDoRegistro;
                    stmtDestino.clearParameters();
                    java.sql.Date vULTIMO_underscoreDIA_underscoreDO_underscoreMES = new java.sql.Date(obterUltimoDiaDoMes(valorPendenteDoPIB.mesEAno).getTime());
                    BigDecimal vPIB_underscoreACUM_underscore12MESES_underscoreREAL = valorPendenteDoPIB.valorDoPIBEmReais.multiply(MILHAO).setScale(0, RoundingMode.DOWN);
                    BigDecimal vPIB_underscoreACUM_underscore12MESES_underscoreDOLAR = valorPendenteDoPIB.valorDoPIBEmDolares.multiply(MILHAO).setScale(0, RoundingMode.DOWN);
                    stmtDestino.setDateAtName("ULTIMO_underscoreDIA_underscoreDO_underscoreMES", vULTIMO_underscoreDIA_underscoreDO_underscoreMES);
                    stmtDestino.setBigDecimalAtName("PIB_underscoreACUM_underscore12MESES_underscoreREAL", vPIB_underscoreACUM_underscore12MESES_underscoreREAL);
                    stmtDestino.setBigDecimalAtName("PIB_underscoreACUM_underscore12MESES_underscoreDOLAR", vPIB_underscoreACUM_underscore12MESES_underscoreDOLAR);
                    int contagemDasInsercoes = stmtDestino.executeUpdate();
                    quantidadeDeRegistrosImportados++;
                    double percentualCompleto = (double) quantidadeDeRegistrosImportados / quantidadeDeRegistrosASeremImportados * 100;
                    pAndamento.setPercentualCompleto((int) percentualCompleto);
                }
                conDestino.commit();
            } catch (Exception ex) {
                conDestino.rollback();
                throw ex;
            } finally {
                if (stmtDestino != null && (!stmtDestino.isClosed())) {
                    stmtDestino.close();
                }
            }
        }
        pAndamento.setPercentualCompleto(100);
    }

