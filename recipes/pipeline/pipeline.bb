SUMMARY = "DISCO 2 Image processing pipeline"
SECTION = "pipeline"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/Lindharden/MARIO.git;branch=main;rev=a2aef1665d58223d31bfdfb03e56fe042113390b"

SRC_URI += " \
    git://github.com/spaceinventor/libcsp.git;protocol=https;destsuffix=git/lib/csp;name=libcsp;branch=master;rev=544635f292b7a15ea46b95cd2861102129c329e7 \
    git://github.com/spaceinventor/libparam.git;protocol=https;destsuffix=git/lib/param;name=libparam;branch=master;rev=fdf62e155a965df99a1012174677c6f2958a7e4f \
"

S = "${WORKDIR}/git"

DEPENDS = "curl openssl libsocketcan can-utils zeromq libyaml meson-native ninja-native pkgconfig python3-pip-native elfutils libbsd protobuf-c"

inherit meson

do_configure() {
    # Remove the previous build directory
    rm -rf ${S}/builddir

    # Create a new build directory
    mkdir -p ${S}/builddir
    meson setup ${S} ${S}/builddir
}

do_compile() {
    # Change to the build directory
    cd ${S}/builddir
    # Compile the project using Ninja
    ninja
}

do_install() {
    # Change back to the source directory
    cd ${S}
    # Install the executable to the appropriate destination
    install -d ${D}${bindir}
    install -m 0755 ${S}/builddir/pipeline_serv ${D}${bindir}
}

FILES_${PN} += "${bindir}/pipeline_serv"