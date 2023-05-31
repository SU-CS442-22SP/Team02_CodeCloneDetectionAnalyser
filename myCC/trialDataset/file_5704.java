    private static QDataSet test3_underscorebinary() throws IOException, StreamException {
        URL url = TestQDataSetStreamHandler.class.getResource("test3.binary.qds");
        QDataSetStreamHandler handler = new QDataSetStreamHandler();
        StreamTool.readStream(Channels.newChannel(url.openStream()), handler);
        QDataSet qds = handler.getDataSet();
        return qds;
    }

