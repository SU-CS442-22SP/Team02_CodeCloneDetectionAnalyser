                @Override
                protected IStatus runCancelableRunnable(IProgressMonitor monitor) {
                    IStatus returnValue = Status.OK_underscoreSTATUS;
                    monitor.beginTask(NLS.bind(Messages.SaveFileOnDiskHandler_underscoreSavingFiles, open), informationUnitsFromExecutionEvent.size());
                    for (InformationUnit informationUnit : informationUnitsFromExecutionEvent) {
                        if (!monitor.isCanceled()) {
                            monitor.setTaskName(NLS.bind(Messages.SaveFileOnDiskHandler_underscoreSaving, informationUnit.getLabel()));
                            InformationStructureRead read = InformationStructureRead.newSession(informationUnit);
                            read.getValueByNodeId(Activator.FILENAME);
                            IFile binaryReferenceFile = InformationUtil.getBinaryReferenceFile(informationUnit);
                            FileWriter writer = null;
                            try {
                                if (binaryReferenceFile != null) {
                                    File file = new File(open, (String) read.getValueByNodeId(Activator.FILENAME));
                                    InputStream contents = binaryReferenceFile.getContents();
                                    writer = new FileWriter(file);
                                    IOUtils.copy(contents, writer);
                                    monitor.worked(1);
                                }
                            } catch (Exception e) {
                                returnValue = StatusCreator.newStatus(NLS.bind(Messages.SaveFileOnDiskHandler_underscoreErrorSaving, informationUnit.getLabel(), e));
                                break;
                            } finally {
                                if (writer != null) {
                                    try {
                                        writer.flush();
                                        writer.close();
                                    } catch (IOException e) {
                                    }
                                }
                            }
                        }
                    }
                    return returnValue;
                }

