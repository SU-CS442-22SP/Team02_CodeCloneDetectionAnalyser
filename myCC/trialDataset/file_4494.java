    public String loadFileContent(final String _underscoreresourceURI) {
        final Lock readLock = this.fileLock.readLock();
        final Lock writeLock = this.fileLock.writeLock();
        boolean hasReadLock = false;
        boolean hasWriteLock = false;
        try {
            readLock.lock();
            hasReadLock = true;
            if (!this.cachedResources.containsKey(_underscoreresourceURI)) {
                readLock.unlock();
                hasReadLock = false;
                writeLock.lock();
                hasWriteLock = true;
                if (!this.cachedResources.containsKey(_underscoreresourceURI)) {
                    final InputStream resourceAsStream = this.getClass().getResourceAsStream(_underscoreresourceURI);
                    final StringWriter writer = new StringWriter();
                    try {
                        IOUtils.copy(resourceAsStream, writer);
                    } catch (final IOException ex) {
                        throw new IllegalStateException("Resource not read-able", ex);
                    }
                    final String loadedResource = writer.toString();
                    this.cachedResources.put(_underscoreresourceURI, loadedResource);
                }
                writeLock.unlock();
                hasWriteLock = false;
                readLock.lock();
                hasReadLock = true;
            }
            return this.cachedResources.get(_underscoreresourceURI);
        } finally {
            if (hasReadLock) {
                readLock.unlock();
            }
            if (hasWriteLock) {
                writeLock.unlock();
            }
        }
    }

