    protected Boolean lancerincident(long idbloc, String Etatbloc, java.util.GregorianCalendar datebloc, long idServeur, String niveau, String message) {
        String codeerr;
        Boolean retour = false;
        Boolean SauvegardeEtatAutocommit;
        int etat;
        acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Appel de la fonction Lancer incident");
        Statement statement = null;
        ResultSet resultat = null;
        String RequeteSQL = "";
        acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - nouvel incident pour le bloc : " + acgtools_underscorecore.AcgIO.RetourneDate(datebloc));
        try {
            this.con = db.OpenConnection();
            SauvegardeEtatAutocommit = this.con.getAutoCommit();
            this.con.setAutoCommit(false);
            if (idbloc == 0) {
                idbloc = this.CreationBloc(idServeur);
                if (idbloc == 0) {
                    retour = false;
                    acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Problème lors de la création du bloc");
                    this.con.rollback();
                    this.con.close();
                    return false;
                }
            }
            acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - bloc : " + idbloc);
            etat = this.ChargerEtatServeur(idbloc, datebloc);
            if (etat != 2) {
                statement = con.createStatement();
                acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Etat chargé");
                RequeteSQL = "SELECT incref_underscoreerr_underscorenumer FROM tbl_underscoreincident_underscoreref " + "WHERE incref_underscorecde_underscorejob ='" + idbloc + "' " + "AND incref_underscoreerr_underscorenumer NOT IN " + "(SELECT incref_underscoreerr_underscorenumer FROM tbl_underscoreincident_underscoreref " + "WHERE incref_underscoreerr_underscoreetat='c') " + "AND incref_underscoreerr_underscorenumer NOT IN " + "(SELECT incenc_underscoreerr_underscorenumer FROM tbl_underscoreincident_underscoreencours " + "WHERE incenc_underscoreerr_underscoreetat='c') ;";
                acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - " + RequeteSQL);
                resultat = statement.executeQuery(RequeteSQL);
                if (!resultat.next()) {
                    resultat.close();
                    RequeteSQL = "INSERT INTO tbl_underscoreincident_underscoreref " + "(incref_underscorecde_underscorejob,incref_underscoreerr_underscoredate,incref_underscoreerr_underscoreetat,incref_underscoreniv_underscorecrimd,incref_underscoreerr_underscoremsg,incref_underscoreerr_underscoresrvnm)" + "VALUES ('" + idbloc + "','" + acgtools_underscorecore.AcgIO.RetourneDate(datebloc) + "','" + Etatbloc + "','" + niveau + "','" + message + "','" + idServeur + "');";
                    acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - " + RequeteSQL);
                    statement.executeUpdate(RequeteSQL);
                    RequeteSQL = "SELECT incref_underscoreerr_underscorenumer FROM tbl_underscoreincident_underscoreref " + "WHERE incref_underscorecde_underscorejob = '" + idbloc + "' " + "AND incref_underscoreerr_underscoresrvnm = '" + idServeur + "' " + "AND incref_underscoreerr_underscoredate = '" + acgtools_underscorecore.AcgIO.RetourneDate(datebloc) + "';";
                    acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - " + RequeteSQL);
                    resultat = statement.executeQuery(RequeteSQL);
                    if (resultat.next()) {
                        codeerr = resultat.getString("incref_underscoreerr_underscorenumer");
                        resultat.close();
                        RequeteSQL = "INSERT INTO tbl_underscoreincident_underscoreencours" + "(incenc_underscoreerr_underscorenumer, incenc_underscoreerr_underscoreetat, incenc_underscoreesc_underscoreetap, " + "incenc_underscoreerr_underscoredate, incenc_underscoretyp_underscoreuser,incenc_underscorecde_underscoreuser,incenc_underscoreerr_underscoremsg,incenc_underscoreniv_underscorecrimd) " + "VALUES ('" + codeerr + "','" + Etatbloc + "',0, " + "'" + acgtools_underscorecore.AcgIO.RetourneDate(datebloc) + "','n',0,'" + message + "','" + niveau + "');";
                        acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - " + RequeteSQL);
                        statement.executeUpdate(RequeteSQL);
                        acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Incident inséré dans la base de données");
                        acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Traitement de l'envois des emails si nécessaire");
                        this.usermail(codeerr, etat, acgtools_underscorecore.AcgIO.RetourneDate(datebloc), message);
                        acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Création de l'historique");
                        this.CreerHistorique(codeerr);
                        acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Créer maj");
                        this.CreerMaj(true);
                        retour = true;
                    } else {
                        acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Problème d'insertion du nouvel incident dans la base");
                        retour = false;
                    }
                } else {
                    codeerr = resultat.getString("incref_underscoreerr_underscorenumer");
                    acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Numéro de l'erreur trouvé. Numéro =" + codeerr);
                    RequeteSQL = "SELECT incenc_underscoreerr_underscoreetat FROM tbl_underscoreincident_underscoreencours " + "WHERE incenc_underscoreerr_underscorenumer='" + codeerr + "';";
                    acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - " + RequeteSQL);
                    resultat = statement.executeQuery(RequeteSQL);
                    if (!resultat.next()) {
                        resultat.close();
                        acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Problème lors de la lecture de l'état de l'incident.");
                        String RequeteSQLInsert = "INSERT INTO tbl_underscoreincident_underscoreencours" + "(incenc_underscoreerr_underscorenumer, incenc_underscoreerr_underscoreetat, incenc_underscoreesc_underscoreetap, " + "incenc_underscoreerr_underscoredate, incenc_underscoretyp_underscoreuser,incenc_underscorecde_underscoreuser,incenc_underscoreerr_underscoremsg,incenc_underscoreniv_underscorecrimd) " + "VALUES ('" + codeerr + "','" + Etatbloc + "',0, " + "'" + acgtools_underscorecore.AcgIO.RetourneDate(datebloc) + "','n',0,'" + "Incident non clotur&eacute; - " + message + "','" + niveau + "');";
                        acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - " + RequeteSQLInsert);
                        statement.execute(RequeteSQLInsert);
                        resultat = statement.executeQuery(RequeteSQL);
                    } else {
                        resultat = statement.executeQuery(RequeteSQL);
                        acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Incident correctement positionné dans encours");
                    }
                    if (resultat.next()) {
                        switch(Etatbloc.charAt(0)) {
                            case 'c':
                                {
                                    acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Cloture de l'incident.");
                                    RequeteSQL = "UPDATE tbl_underscoreincident_underscoreref SET incref_underscoreerr_underscoreetat='c'" + "WHERE incref_underscoreerr_underscorenumer='" + codeerr + "';";
                                    acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - " + RequeteSQL);
                                    statement.executeUpdate(RequeteSQL);
                                    this.UpdateEnCours(codeerr, "c", niveau, acgtools_underscorecore.AcgIO.RetourneDate(datebloc), message, "auto");
                                    acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Traitement de l'envois des emails si nécessaire");
                                    this.usermail(codeerr, etat, message, acgtools_underscorecore.AcgIO.RetourneDate(datebloc));
                                    acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Créer maj");
                                    this.CreerMaj(false);
                                    retour = true;
                                    break;
                                }
                            case 'm':
                                {
                                    this.UpdateEnCours(codeerr, "m", niveau, acgtools_underscorecore.AcgIO.RetourneDate(datebloc), message, "auto");
                                    acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Traitement de l'envois des emails si nécessaire");
                                    this.usermail(codeerr, etat, message, acgtools_underscorecore.AcgIO.RetourneDate(datebloc));
                                    acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Créer maj");
                                    this.CreerMaj(false);
                                    retour = true;
                                    break;
                                }
                            default:
                                {
                                    this.UpdateEnCours(codeerr, "m", niveau, acgtools_underscorecore.AcgIO.RetourneDate(datebloc), message, "");
                                    acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Traitement de l'envois des emails si nécessaire");
                                    this.usermail(codeerr, etat, message, acgtools_underscorecore.AcgIO.RetourneDate(datebloc));
                                    acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Créer maj");
                                    this.CreerMaj(false);
                                    retour = true;
                                    break;
                                }
                        }
                    } else {
                        acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Problème lors de la lecture de l'état de l'incident.");
                        retour = false;
                    }
                }
            } else {
                acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Systeme en maintenance, pas de remontée d'incidents.");
                retour = false;
            }
        } catch (ClassNotFoundException ex) {
            acgtools_underscorecore.AcgIO.SortieLog(new Date() + "Annulation des modifications.");
            con.rollback();
            acgtools_underscorecore.AcgIO.SortieLog(new Date() + "Probléme lors de l'éxécution de la connexion.");
            acgtools_underscorecore.AcgIO.SortieLog(ex.getMessage());
            retour = false;
        } catch (SQLException ex) {
            acgtools_underscorecore.AcgIO.SortieLog(new Date() + "Annulation des modifications.");
            con.rollback();
            acgtools_underscorecore.AcgIO.SortieLog(ex.getMessage());
            acgtools_underscorecore.AcgIO.SortieLog(new Date() + "Probléme lors de l'éxécution de la requète SQL :");
            acgtools_underscorecore.AcgIO.SortieLog(RequeteSQL);
            retour = false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (retour) {
                    con.commit();
                    acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Création de l'incident : succès");
                } else {
                    con.rollback();
                    acgtools_underscorecore.AcgIO.SortieLog(new Date() + " - Création de l'incident : echec");
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                acgtools_underscorecore.AcgIO.SortieLog(new Date() + "Problème lors de la fermeture de la connection à la base de données");
            }
            return retour;
        }
    }

