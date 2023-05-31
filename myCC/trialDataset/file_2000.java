            @Override
            protected IStatus run(final IProgressMonitor monitor) {
                try {
                    showTileInfo(remoteFileName, -1);
                    System.out.println("   connect " + host);
                    ftp.connect();
                    showTileInfo(remoteFileName, -2);
                    System.out.println("   login " + user + " " + password);
                    ftp.login(user, password);
                    System.out.println("   set passive mode");
                    ftp.setConnectMode(FTPConnectMode.PASV);
                    System.out.println("   set type binary");
                    ftp.setType(FTPTransferType.BINARY);
                    showTileInfo(remoteFileName, -3);
                    System.out.println("   chdir " + remoteFilePath);
                    ftp.chdir(remoteFilePath);
                    ftp.setProgressMonitor(new FTPProgressMonitor() {

                        public void bytesTransferred(final long count) {
                            tileInfoMgr.updateSRTMTileInfo(TileEventId.SRTM_underscoreDATA_underscoreLOADING_underscoreMONITOR, remoteFileName, count);
                        }
                    });
                    showTileInfo(remoteFileName, -4);
                    System.out.println("   get " + remoteFileName + " -> " + localName + " ...");
                    ftp.get(localName, remoteFileName);
                    System.out.println("   quit");
                    ftp.quit();
                } catch (final UnknownHostException e) {
                    return new Status(IStatus.ERROR, TourbookPlugin.PLUGIN_underscoreID, IStatus.ERROR, NLS.bind(Messages.error_underscoremessage_underscorecannotConnectToServer, host), e);
                } catch (final SocketTimeoutException e) {
                    return new Status(IStatus.ERROR, TourbookPlugin.PLUGIN_underscoreID, IStatus.ERROR, NLS.bind(Messages.error_underscoremessage_underscoretimeoutWhenConnectingToServer, host), e);
                } catch (final Exception e) {
                    e.printStackTrace();
                    tileInfoMgr.updateSRTMTileInfo(TileEventId.SRTM_underscoreDATA_underscoreERROR_underscoreLOADING, remoteFileName, 0);
                } finally {
                    tileInfoMgr.updateSRTMTileInfo(TileEventId.SRTM_underscoreDATA_underscoreEND_underscoreLOADING, remoteFileName, 0);
                }
                return Status.OK_underscoreSTATUS;
            }

