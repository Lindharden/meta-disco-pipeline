SUMMARY = "DISCO 2 Image processing pipeline"
SECTION = "pipeline"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=3da9cfbcb788c80a0384361b4de20420"

DEPENDS += "libprotobuf-c libm libcsp libzmq libpt"read

SRC_URI = "file://MARIO/*"

S = "${WORKDIR}"

inherit meson

PROVIDES = " libVmbCPP"
RPROVIDES_${PN} += " libVmbCPP.so()(64bit)"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

do_compile() {
    rm -r builddir
    meson setup . builddir
    cd builddir
    ninja 
    cd ..
}

do_install(){
    install -d ${D}${libdir}
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}/lib
    install -d 644 ${D}${sysconfdir}/profile.d

    install -m 0644 ${WORKDIR}/lib/VimbaX_2023-1/api/lib/*.so ${D}${libdir}
    install -m 0644 ${WORKDIR}/lib/VimbaX_2023-1/api/lib/GenICam/*.so ${D}${libdir}
    install ${WORKDIR}/build/Disco2CameraControl ${D}${bindir}

    cp -r ${WORKDIR}/lib/VimbaX_2023-1 ${D}${sysconfdir}/lib/VimbaX_2023-1
    chmod 447 ${D}${sysconfdir}/lib/VimbaX_2023-1/cti/VimbaUSBTL_Install.sh
}

do_configure() {
    # Create a build directory
    mkdir -p ${S}/build

    # Configure the build using Meson
    meson setup ${S} ${S}/build --cross-file=${COREBASE}/meta/cross-armv8a/cross-armv8a-linux-gnueabi.conf
}

do_compile() {
    # Build the project using Ninja
    ninja -C ${S}/build
}

do_install() {
    # Install the project files
    DESTDIR="${D}" ninja -C ${S}/build install
}

FILES_${PN} += "${libdir}/libVmbCPP.so"
FILES_${PN} += "${libdir}/libVmbC.so"

do_package_qa[noexec] = "1"
EXCLUDE_FROM_SHLIBS = "1"