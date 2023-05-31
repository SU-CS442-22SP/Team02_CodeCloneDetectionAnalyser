        @Override
        public void run() {
            try {
                IOUtils.copy(_underscoreis, processOutStr);
            } catch (final IOException ioe) {
                proc.destroy();
            } finally {
                IOUtils.closeQuietly(_underscoreis);
                IOUtils.closeQuietly(processOutStr);
            }
        }

