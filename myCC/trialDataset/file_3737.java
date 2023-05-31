    @Test
    public void test_underscorelookupType() throws Exception {
        URL url = new URL(baseUrl + "/lookupType/Tri");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("[{\"itemTypeID\":25595,\"itemCategoryID\":4,\"name\":\"Alloyed Tritanium Bar\",\"icon\":\"69_underscore11\"},{\"itemTypeID\":21729,\"itemCategoryID\":17,\"name\":\"Angel Advanced Trigger Mechanism\",\"icon\":\"55_underscore13\"},{\"itemTypeID\":21731,\"itemCategoryID\":17,\"name\":\"Angel Simple Trigger Mechanism\",\"icon\":\"55_underscore13\"},{\"itemTypeID\":21730,\"itemCategoryID\":17,\"name\":\"Angel Standard Trigger Mechanism\",\"icon\":\"55_underscore13\"},{\"itemTypeID\":28830,\"itemCategoryID\":17,\"name\":\"Brutor Tribe Roster\",\"icon\":\"34_underscore06\"},{\"itemTypeID\":29105,\"itemCategoryID\":17,\"name\":\"Capital Thermonuclear Trigger Unit\",\"icon\":\"41_underscore07\"},{\"itemTypeID\":29106,\"itemCategoryID\":9,\"name\":\"Capital Thermonuclear Trigger Unit Blueprint\",\"icon\":\"03_underscore02\"},{\"itemTypeID\":28390,\"itemCategoryID\":25,\"name\":\"Compressed Triclinic Bistot\",\"icon\":\"71_underscore02\"},{\"itemTypeID\":28451,\"itemCategoryID\":9,\"name\":\"Compressed Triclinic Bistot Blueprint\"},{\"itemTypeID\":2979,\"itemCategoryID\":17,\"name\":\"Crate of Industrial-Grade Tritanium-Alloy Scraps\",\"icon\":\"45_underscore10\"},{\"itemTypeID\":2980,\"itemCategoryID\":17,\"name\":\"Large Crate of Industrial-Grade Tritanium-Alloy Scraps\",\"icon\":\"45_underscore10\"},{\"itemTypeID\":25894,\"itemCategoryID\":7,\"name\":\"Large Trimark Armor Pump I\",\"icon\":\"68_underscore10\",\"metaLevel\":0},{\"itemTypeID\":25895,\"itemCategoryID\":9,\"name\":\"Large Trimark Armor Pump I Blueprint\",\"icon\":\"02_underscore10\"},{\"itemTypeID\":26302,\"itemCategoryID\":7,\"name\":\"Large Trimark Armor Pump II\",\"icon\":\"68_underscore10\",\"metaLevel\":5},{\"itemTypeID\":26303,\"itemCategoryID\":9,\"name\":\"Large Trimark Armor Pump II Blueprint\",\"icon\":\"02_underscore10\"},{\"itemTypeID\":31055,\"itemCategoryID\":7,\"name\":\"Medium Trimark Armor Pump I\",\"icon\":\"68_underscore10\"},{\"itemTypeID\":31056,\"itemCategoryID\":9,\"name\":\"Medium Trimark Armor Pump I Blueprint\",\"icon\":\"02_underscore10\"},{\"itemTypeID\":31059,\"itemCategoryID\":7,\"name\":\"Medium Trimark Armor Pump II\",\"icon\":\"68_underscore10\"},{\"itemTypeID\":31060,\"itemCategoryID\":9,\"name\":\"Medium Trimark Armor Pump II Blueprint\",\"icon\":\"02_underscore10\"},{\"itemTypeID\":30987,\"itemCategoryID\":7,\"name\":\"Small Trimark Armor Pump I\",\"icon\":\"68_underscore10\"},{\"itemTypeID\":30988,\"itemCategoryID\":9,\"name\":\"Small Trimark Armor Pump I Blueprint\",\"icon\":\"02_underscore10\"},{\"itemTypeID\":31057,\"itemCategoryID\":7,\"name\":\"Small Trimark Armor Pump II\",\"icon\":\"68_underscore10\"},{\"itemTypeID\":31058,\"itemCategoryID\":9,\"name\":\"Small Trimark Armor Pump II Blueprint\",\"icon\":\"02_underscore10\"},{\"itemTypeID\":25593,\"itemCategoryID\":4,\"name\":\"Smashed Trigger Unit\",\"icon\":\"69_underscore13\"},{\"itemTypeID\":23147,\"itemCategoryID\":17,\"name\":\"Takmahl Tri-polished Lens\",\"icon\":\"55_underscore16\"},{\"itemTypeID\":26842,\"itemCategoryID\":6,\"name\":\"Tempest Tribal Issue\",\"metaLevel\":6},{\"itemTypeID\":11691,\"itemCategoryID\":17,\"name\":\"Thermonuclear Trigger Unit\",\"icon\":\"41_underscore07\"},{\"itemTypeID\":17322,\"itemCategoryID\":9,\"name\":\"Thermonuclear Trigger Unit Blueprint\",\"icon\":\"03_underscore02\"},{\"itemTypeID\":22140,\"itemCategoryID\":17,\"name\":\"Tri-Vitoc\",\"icon\":\"11_underscore15\"},{\"itemTypeID\":27951,\"itemCategoryID\":7,\"name\":\"Triage Module I\",\"icon\":\"70_underscore10\",\"metaLevel\":0},{\"itemTypeID\":27952,\"itemCategoryID\":9,\"name\":\"Triage Module I Blueprint\",\"icon\":\"06_underscore03\"},{\"itemTypeID\":17428,\"itemCategoryID\":25,\"name\":\"Triclinic Bistot\",\"icon\":\"23_underscore06\"},{\"itemTypeID\":25612,\"itemCategoryID\":4,\"name\":\"Trigger Unit\",\"icon\":\"69_underscore14\"},{\"itemTypeID\":11066,\"itemCategoryID\":17,\"name\":\"Trinary Data\",\"icon\":\"34_underscore05\"},{\"itemTypeID\":16307,\"itemCategoryID\":7,\"name\":\"Triple-sheathed Adaptive Nano Plating I\",\"icon\":\"01_underscore08\",\"metaLevel\":2},{\"itemTypeID\":16315,\"itemCategoryID\":7,\"name\":\"Triple-sheathed Magnetic Plating I\",\"icon\":\"01_underscore08\",\"metaLevel\":2},{\"itemTypeID\":16323,\"itemCategoryID\":7,\"name\":\"Triple-sheathed Reactive Plating I\",\"icon\":\"01_underscore08\",\"metaLevel\":2},{\"itemTypeID\":16331,\"itemCategoryID\":7,\"name\":\"Triple-sheathed Reflective Plating I\",\"icon\":\"01_underscore08\",\"metaLevel\":2},{\"itemTypeID\":16347,\"itemCategoryID\":7,\"name\":\"Triple-sheathed Regenerative Plating I\",\"icon\":\"01_underscore08\",\"metaLevel\":2},{\"itemTypeID\":16339,\"itemCategoryID\":7,\"name\":\"Triple-sheathed Thermic Plating I\",\"icon\":\"01_underscore08\",\"metaLevel\":2},{\"itemTypeID\":25598,\"itemCategoryID\":4,\"name\":\"Tripped Power Circuit\",\"icon\":\"69_underscore15\"},{\"itemTypeID\":593,\"itemCategoryID\":6,\"name\":\"Tristan\",\"metaLevel\":0},{\"itemTypeID\":940,\"itemCategoryID\":9,\"name\":\"Tristan Blueprint\"},{\"itemTypeID\":17916,\"itemCategoryID\":17,\"name\":\"Tritan\\u0027s Forked Key\",\"icon\":\"34_underscore06\"},{\"itemTypeID\":34,\"itemCategoryID\":4,\"name\":\"Tritanium\",\"icon\":\"06_underscore14\"},{\"itemTypeID\":23170,\"itemCategoryID\":17,\"name\":\"Yan Jung Trigonometric Laws\",\"icon\":\"55_underscore12\"}]"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("application/json; charset=utf-8"));
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/xml");
        assertThat(connection.getResponseCode(), equalTo(200));
        assertThat(getResponse(connection), equalTo("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><rowset><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>69_underscore11</icon><itemCategoryID>4</itemCategoryID><itemTypeID>25595</itemTypeID><name>Alloyed Tritanium Bar</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>55_underscore13</icon><itemCategoryID>17</itemCategoryID><itemTypeID>21729</itemTypeID><name>Angel Advanced Trigger Mechanism</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>55_underscore13</icon><itemCategoryID>17</itemCategoryID><itemTypeID>21731</itemTypeID><name>Angel Simple Trigger Mechanism</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>55_underscore13</icon><itemCategoryID>17</itemCategoryID><itemTypeID>21730</itemTypeID><name>Angel Standard Trigger Mechanism</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>34_underscore06</icon><itemCategoryID>17</itemCategoryID><itemTypeID>28830</itemTypeID><name>Brutor Tribe Roster</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>41_underscore07</icon><itemCategoryID>17</itemCategoryID><itemTypeID>29105</itemTypeID><name>Capital Thermonuclear Trigger Unit</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>03_underscore02</icon><itemCategoryID>9</itemCategoryID><itemTypeID>29106</itemTypeID><name>Capital Thermonuclear Trigger Unit Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>71_underscore02</icon><itemCategoryID>25</itemCategoryID><itemTypeID>28390</itemTypeID><name>Compressed Triclinic Bistot</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><itemCategoryID>9</itemCategoryID><itemTypeID>28451</itemTypeID><name>Compressed Triclinic Bistot Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>45_underscore10</icon><itemCategoryID>17</itemCategoryID><itemTypeID>2979</itemTypeID><name>Crate of Industrial-Grade Tritanium-Alloy Scraps</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>45_underscore10</icon><itemCategoryID>17</itemCategoryID><itemTypeID>2980</itemTypeID><name>Large Crate of Industrial-Grade Tritanium-Alloy Scraps</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>68_underscore10</icon><itemCategoryID>7</itemCategoryID><itemTypeID>25894</itemTypeID><metaLevel>0</metaLevel><name>Large Trimark Armor Pump I</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>02_underscore10</icon><itemCategoryID>9</itemCategoryID><itemTypeID>25895</itemTypeID><name>Large Trimark Armor Pump I Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>68_underscore10</icon><itemCategoryID>7</itemCategoryID><itemTypeID>26302</itemTypeID><metaLevel>5</metaLevel><name>Large Trimark Armor Pump II</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>02_underscore10</icon><itemCategoryID>9</itemCategoryID><itemTypeID>26303</itemTypeID><name>Large Trimark Armor Pump II Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>68_underscore10</icon><itemCategoryID>7</itemCategoryID><itemTypeID>31055</itemTypeID><name>Medium Trimark Armor Pump I</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>02_underscore10</icon><itemCategoryID>9</itemCategoryID><itemTypeID>31056</itemTypeID><name>Medium Trimark Armor Pump I Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>68_underscore10</icon><itemCategoryID>7</itemCategoryID><itemTypeID>31059</itemTypeID><name>Medium Trimark Armor Pump II</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>02_underscore10</icon><itemCategoryID>9</itemCategoryID><itemTypeID>31060</itemTypeID><name>Medium Trimark Armor Pump II Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>68_underscore10</icon><itemCategoryID>7</itemCategoryID><itemTypeID>30987</itemTypeID><name>Small Trimark Armor Pump I</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>02_underscore10</icon><itemCategoryID>9</itemCategoryID><itemTypeID>30988</itemTypeID><name>Small Trimark Armor Pump I Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>68_underscore10</icon><itemCategoryID>7</itemCategoryID><itemTypeID>31057</itemTypeID><name>Small Trimark Armor Pump II</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>02_underscore10</icon><itemCategoryID>9</itemCategoryID><itemTypeID>31058</itemTypeID><name>Small Trimark Armor Pump II Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>69_underscore13</icon><itemCategoryID>4</itemCategoryID><itemTypeID>25593</itemTypeID><name>Smashed Trigger Unit</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>55_underscore16</icon><itemCategoryID>17</itemCategoryID><itemTypeID>23147</itemTypeID><name>Takmahl Tri-polished Lens</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><itemCategoryID>6</itemCategoryID><itemTypeID>26842</itemTypeID><metaLevel>6</metaLevel><name>Tempest Tribal Issue</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>41_underscore07</icon><itemCategoryID>17</itemCategoryID><itemTypeID>11691</itemTypeID><name>Thermonuclear Trigger Unit</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>03_underscore02</icon><itemCategoryID>9</itemCategoryID><itemTypeID>17322</itemTypeID><name>Thermonuclear Trigger Unit Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>11_underscore15</icon><itemCategoryID>17</itemCategoryID><itemTypeID>22140</itemTypeID><name>Tri-Vitoc</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>70_underscore10</icon><itemCategoryID>7</itemCategoryID><itemTypeID>27951</itemTypeID><metaLevel>0</metaLevel><name>Triage Module I</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>06_underscore03</icon><itemCategoryID>9</itemCategoryID><itemTypeID>27952</itemTypeID><name>Triage Module I Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>23_underscore06</icon><itemCategoryID>25</itemCategoryID><itemTypeID>17428</itemTypeID><name>Triclinic Bistot</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>69_underscore14</icon><itemCategoryID>4</itemCategoryID><itemTypeID>25612</itemTypeID><name>Trigger Unit</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>34_underscore05</icon><itemCategoryID>17</itemCategoryID><itemTypeID>11066</itemTypeID><name>Trinary Data</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>01_underscore08</icon><itemCategoryID>7</itemCategoryID><itemTypeID>16307</itemTypeID><metaLevel>2</metaLevel><name>Triple-sheathed Adaptive Nano Plating I</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>01_underscore08</icon><itemCategoryID>7</itemCategoryID><itemTypeID>16315</itemTypeID><metaLevel>2</metaLevel><name>Triple-sheathed Magnetic Plating I</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>01_underscore08</icon><itemCategoryID>7</itemCategoryID><itemTypeID>16323</itemTypeID><metaLevel>2</metaLevel><name>Triple-sheathed Reactive Plating I</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>01_underscore08</icon><itemCategoryID>7</itemCategoryID><itemTypeID>16331</itemTypeID><metaLevel>2</metaLevel><name>Triple-sheathed Reflective Plating I</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>01_underscore08</icon><itemCategoryID>7</itemCategoryID><itemTypeID>16347</itemTypeID><metaLevel>2</metaLevel><name>Triple-sheathed Regenerative Plating I</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>01_underscore08</icon><itemCategoryID>7</itemCategoryID><itemTypeID>16339</itemTypeID><metaLevel>2</metaLevel><name>Triple-sheathed Thermic Plating I</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>69_underscore15</icon><itemCategoryID>4</itemCategoryID><itemTypeID>25598</itemTypeID><name>Tripped Power Circuit</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><itemCategoryID>6</itemCategoryID><itemTypeID>593</itemTypeID><metaLevel>0</metaLevel><name>Tristan</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><itemCategoryID>9</itemCategoryID><itemTypeID>940</itemTypeID><name>Tristan Blueprint</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>34_underscore06</icon><itemCategoryID>17</itemCategoryID><itemTypeID>17916</itemTypeID><name>Tritan's Forked Key</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>06_underscore14</icon><itemCategoryID>4</itemCategoryID><itemTypeID>34</itemTypeID><name>Tritanium</name></row><row xsi:type=\"invTypeBasicInfoDto\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><icon>55_underscore12</icon><itemCategoryID>17</itemCategoryID><itemTypeID>23170</itemTypeID><name>Yan Jung Trigonometric Laws</name></row></rowset>"));
        assertThat(connection.getHeaderField("Content-Type"), equalTo("application/xml; charset=utf-8"));
    }

