AC_INIT(jblux_server, 1.0, jonescaseyb@gmail.com)
AM_INIT_AUTOMAKE(jblux_server, 1.0)
AC_CONFIG_HEADERS(src/config.h)
AC_PROG_INSTALL
AC_CONFIG_MACRO_DIR([m4])
AC_PREFIX_DEFAULT(/usr/local)

AC_PROG_CPP
AC_PROG_CC
AC_LANG_C
AC_HEADER_STDC
AM_PROG_CC_C_O

PKG_CHECK_MODULES(CRYPTO, libcrypto)
PKG_CHECK_MODULES(SSL, libssl)
PKG_CHECK_MODULES(ZLIB, zlib)
AC_CHECK_LIB(m, sqrt, [], [
        echo "Error!  libm required."
        exit -1
        ])
AC_CHECK_LIB(pthread, pthread_create, [], [
        echo "Error!  pthread support required."
        exit -1
        ])
AC_CHECK_LIB(yajl, yajl_gen_map_open, [], [
        echo "Error!  yajl required."
        exit -1
        ])
AC_CHECK_LIB(pq, PQconnectdb, [], [
        echo "Error!  PostgreSQL with libpq required."
        exit -1
        ])

AC_CONFIG_FILES([Makefile bin/Makefile])
AC_OUTPUT

