    public DatabaseDefinitionFactory(final DBIf db, final String adapter) throws IOException {
        _underscoredb = db;
        LOG.debug("Loading adapter: " + adapter);
        final URL url = getClass().getClassLoader().getResource("adapter/" + adapter + ".properties");
        _underscoreprops = new Properties();
        _underscoreprops.load(url.openStream());
        if (adapter.equals("mysql")) {
            _underscoremodifier = new MySQLModifier(this);
        } else if (adapter.equals("postgresql")) {
            _underscoremodifier = new PostgresModifier(this);
        } else if (adapter.equals("hypersonic")) {
            _underscoremodifier = new HSQLModifier(this);
        } else if (adapter.equals("oracle")) {
            _underscoremodifier = new OracleModifier(this);
        } else if (adapter.equals("mssql")) {
            _underscoremodifier = new MSSQLModifier(this);
        } else {
            _underscoremodifier = null;
        }
    }

