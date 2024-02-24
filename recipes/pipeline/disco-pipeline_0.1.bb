SUMMARY = "DISCO 2 Image processing pipeline"
SECTION = "pipeline"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS += "libprotobuf-c libm libcsp libzmq libpt"

SRCREV = "${AUTOREV}"
SRC_URI = "git://github.com/Lindharden/MARIO.git;branch=external_config"

S = "${WORKDIR}"

inherit meson

do_configure_prepend() {
    # Remove the previous build directory
    rm -rf ${S}/builddir
}

do_configure() {
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