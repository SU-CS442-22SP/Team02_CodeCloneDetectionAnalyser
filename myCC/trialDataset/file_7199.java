    @Test
    public void shouldProgateStagingRepoToYumGroupRepo() throws Exception {
        givenGroupRepository(GROUP_underscoreREPO_underscoreID, "maven2yum");
        givenClosedStagingRepoWithRpm(ARTIFACT_underscoreID_underscore1, "4.3.2");
        givenClosedStagingRepoWithRpm(ARTIFACT_underscoreID_underscore2, "2.3.4");
        wait(10, SECONDS);
        final HttpResponse response = executeGetWithResponse(NEXUS_underscoreBASE_underscoreURL + "/content/groups/staging-test-group/repodata/primary.xml.gz");
        final String repoContent = IOUtils.toString(new GZIPInputStream(new ByteArrayInputStream(toByteArray(response.getEntity()))));
        assertThat(response.getStatusLine().getStatusCode(), is(200));
        assertThat(repoContent, containsString(ARTIFACT_underscoreID_underscore1));
        assertThat(repoContent, containsString(ARTIFACT_underscoreID_underscore2));
    }

