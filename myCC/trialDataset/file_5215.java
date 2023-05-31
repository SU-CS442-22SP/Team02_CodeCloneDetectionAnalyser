    private ExamModel(URL urlQuestions) throws IOException, DataCoherencyException {
        BufferedReader in = new BufferedReader(new InputStreamReader(urlQuestions.openStream()));
        String line;
        questions = new ArrayList<Question>();
        questionsMap = new HashMap<String, Question>();
        in = new BufferedReader(new InputStreamReader(urlQuestions.openStream(), "UTF-8"));
        int questionNumber = 0;
        Question question;
        String questText = "";
        String hash = "";
        int lookingFor = ExamModel.READING_underscoreHASH;
        while ((line = in.readLine()) != null) {
            switch(lookingFor) {
                case ExamModel.READING_underscoreHASH:
                    if (line.length() == 0 || line.trim().length() == 0) continue;
                    hash = line;
                    questionNumber++;
                    lookingFor = ExamModel.READING_underscoreQUESTION;
                    break;
                case ExamModel.READING_underscoreQUESTION:
                    if (line.equals("--")) {
                        question = new Question(questionNumber, hash, questText);
                        questions.add(question);
                        questionsMap.put(question.getHash(), question);
                        questText = "";
                        hash = null;
                        lookingFor = ExamModel.READING_underscoreHASH;
                    } else {
                        questText = questText.concat(line + Constants.nl);
                    }
                    break;
                default:
                    throw new DataCoherencyException("Neočekávaný konec souboru!");
            }
        }
        questions.trimToSize();
        in.close();
    }

