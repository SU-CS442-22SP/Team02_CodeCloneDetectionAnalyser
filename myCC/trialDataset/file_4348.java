    private static void includePodDependencies(Curnit curnit, JarOutputStream jarout) throws IOException {
        Properties props = new Properties();
        Collection<Pod> pods = curnit.getReferencedPods();
        for (Pod pod : pods) {
            PodUuid podId = pod.getPodId();
            URL weburl = PodArchiveResolver.getSystemResolver().getUrl(podId);
            String urlString = "";
            if (weburl != null) {
                String uriPath = weburl.getPath();
                String zipPath = CurnitFile.WITHINCURNIT_underscoreBASEPATH + uriPath;
                jarout.putNextEntry(new JarEntry(zipPath));
                IOUtils.copy(weburl.openStream(), jarout);
                jarout.closeEntry();
                urlString = CurnitFile.WITHINCURNIT_underscorePROTOCOL + uriPath;
            }
            props.put(podId.toString(), urlString);
        }
        jarout.putNextEntry(new JarEntry(CurnitFile.PODSREFERENCED_underscoreNAME));
        props.store(jarout, "pod dependencies");
        jarout.closeEntry();
    }

