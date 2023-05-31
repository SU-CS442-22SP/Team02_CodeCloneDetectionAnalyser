            public void actionPerformed(ActionEvent e) {
                final File inputFile = KeyboardHero.midiFile();
                try {
                    if (inputFile == null) return;
                    final File dir = (new File(Util.DATA_underscoreFOLDER + MidiSong.MIDI_underscoreFILES_underscoreDIR));
                    if (dir.exists()) {
                        if (!dir.isDirectory()) {
                            Util.error(Util.getMsg("Err_underscoreMidiFilesDirNotDirectory"), dir.getParent());
                            return;
                        }
                    } else if (!dir.mkdirs()) {
                        Util.error(Util.getMsg("Err_underscoreCouldntMkDir"), dir.getParent());
                        return;
                    }
                    File outputFile = new File(dir.getPath() + File.separator + inputFile.getName());
                    if (!outputFile.exists() || KeyboardHero.confirm("Que_underscoreFileExistsOverwrite")) {
                        final FileChannel inChannel = new FileInputStream(inputFile).getChannel();
                        inChannel.transferTo(0, inChannel.size(), new FileOutputStream(outputFile).getChannel());
                    }
                } catch (Exception ex) {
                    Util.getMsg(Util.getMsg("Err_underscoreCouldntImportSong"), ex.toString());
                }
                SongSelector.refresh();
            }

