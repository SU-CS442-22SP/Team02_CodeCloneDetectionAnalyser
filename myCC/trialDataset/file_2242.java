    @HttpAction(name = "map.calibrate", method = { HttpAction.Method.post }, responseType = "text/plain", parameters = { @HttpParameter(name = "user"), @HttpParameter(name = "guid"), @HttpParameter(name = "uploadFile"), @HttpParameter(name = "mapUrl"), @HttpParameter(name = "mapSource"), @HttpParameter(name = "south"), @HttpParameter(name = "west"), @HttpParameter(name = "north"), @HttpParameter(name = "east") })
    @HttpAuthentication(method = { HttpAuthentication.Method.WSSE })
    public String calibrate(User user, String guid, Collection<FileItem> uploadFile, String mapUrl, String mapSource, String south, String west, String north, String east) throws HttpRpcException {
        GenericDAO<UserMapOriginal> dao = DAOFactory.createDAO(UserMapOriginal.class);
        try {
            TransactionManager.beginTransaction();
        } catch (Throwable e) {
            logger.error(e);
            return "FAIL";
        }
        try {
            final UserMapOriginal mapOriginal = dao.findUniqueByCriteria(Expression.eq("guid", guid));
            if (mapOriginal == null) throw new HttpRpcException(ErrorConstant.ERROR_underscoreNOT_underscoreFOUND, "map");
            if (UserMapOriginal.SubState.INPROC.equals(mapOriginal.getSubstate())) throw new HttpRpcException(ErrorConstant.ERROR_underscoreILLEGAL_underscoreOBJECT_underscoreSTATE, "map");
            if (UserMapOriginal.State.COMBINE.equals(mapOriginal.getState())) throw new HttpRpcException(ErrorConstant.ERROR_underscoreILLEGAL_underscoreOBJECT_underscoreSTATE, "map");
            if (!"download".equals(mapSource) && !"upload".equals(mapSource) && !"current".equals(mapSource)) throw new HttpRpcException(ErrorConstant.ERROR_underscoreINVALID_underscoreOBJECT, "mapSource");
            try {
                mapOriginal.setSWLat(Double.parseDouble(south));
            } catch (Throwable t) {
                throw new HttpRpcException(ErrorConstant.ERROR_underscoreINVALID_underscoreOBJECT, "south");
            }
            try {
                mapOriginal.setSWLon(Double.parseDouble(west));
            } catch (Throwable t) {
                throw new HttpRpcException(ErrorConstant.ERROR_underscoreINVALID_underscoreOBJECT, "west");
            }
            try {
                mapOriginal.setNELat(Double.parseDouble(north));
            } catch (Throwable t) {
                throw new HttpRpcException(ErrorConstant.ERROR_underscoreINVALID_underscoreOBJECT, "north");
            }
            try {
                mapOriginal.setNELon(Double.parseDouble(east));
            } catch (Throwable t) {
                throw new HttpRpcException(ErrorConstant.ERROR_underscoreINVALID_underscoreOBJECT, "east");
            }
            mapOriginal.setState(UserMapOriginal.State.CALIBRATE);
            mapOriginal.setSubstate(UserMapOriginal.SubState.INPROC);
            final XFile mapStorage = new XFile(new XFile(Configuration.getInstance().getPrivateMapStorage().toString()), mapOriginal.getGuid());
            mapStorage.mkdir();
            if ("download".equals(mapSource)) {
                final XFile tmpFile;
                final URI uri = new URI(mapUrl);
                String query = (StringUtils.isEmpty(uri.getQuery())) ? "?BBOX=" : "&BBOX=";
                query += west + "," + south + "," + east + "," + north;
                URLConnection con = (new URL(mapUrl + query)).openConnection();
                if (con == null || con.getContentLength() == 0) throw new HttpRpcException(ErrorConstant.ERROR_underscoreINVALID_underscoreRESOURCE, "mapUrl");
                if (!con.getContentType().startsWith("image/")) throw new HttpRpcException(ErrorConstant.ERROR_underscoreINVALID_underscoreOBJECT_underscoreTYPE, "mapUrl");
                tmpFile = new XFile(mapStorage, mapOriginal.getGuid());
                XFileOutputStream out = new XFileOutputStream(tmpFile);
                IOUtils.copy(con.getInputStream(), out);
                out.flush();
                out.close();
            } else if ("upload".equals(mapSource)) {
                final XFile tmpFile;
                final FileItem file = uploadFile.iterator().next();
                if (file == null || file.getSize() == 0) throw new HttpRpcException(ErrorConstant.ERROR_underscoreINVALID_underscoreRESOURCE, "uploadFile");
                if (!file.getContentType().startsWith("image/")) throw new HttpRpcException(ErrorConstant.ERROR_underscoreINVALID_underscoreOBJECT_underscoreTYPE, "uploadFile");
                tmpFile = new XFile(mapStorage, mapOriginal.getGuid());
                XFileOutputStream out = new XFileOutputStream(tmpFile);
                IOUtils.copy(file.getInputStream(), out);
                out.flush();
                out.close();
            } else if ("current".equals(mapSource)) {
            }
            dao.update(mapOriginal);
            TransactionManager.commitTransaction();
            try {
                PoolClientInterface pool = PoolFactory.getInstance().getClientPool();
                if (pool == null) throw ErrorConstant.EXCEPTION_underscoreINTERNAL;
                pool.put(mapOriginal, new StatesStack(new byte[] { 0x00, 0x18 }), GeneralCompleteStrategy.class);
            } catch (Throwable t) {
                logger.error(t);
            }
            return "SUCCESS";
        } catch (HttpRpcException e) {
            TransactionManager.rollbackTransaction();
            logger.error(e);
            return "FAIL";
        } catch (Throwable e) {
            logger.error(e);
            TransactionManager.rollbackTransaction();
            return "FAIL";
        }
    }

