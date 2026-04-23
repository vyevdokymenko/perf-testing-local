package utils

import net.datafaker.Faker
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream

import java.nio.file.DirectoryStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class Utils {
    static getOutputFile(String dir, String fileName) {
        def outputDir = Paths.get(dir)
        if (!Files.exists(outputDir)) Files.createDirectory(outputDir)
        return outputDir.resolve(fileName).toFile()
    }

    static getRandomFemale() {
        getRandomName(false)
    }

    static getRandomMale() {
        return getRandomName(true)
    }

    static getRandomPerson() {
        return getRandomName(new Random().nextBoolean())
    }

    private static getRandomName(boolean isMale) {
        Faker faker = new Faker(new Locale("uk_UA"))
        def femalePatronymics = ["Іванівна", "Петрівна", "Олександрівна", "Василівна", "Миколаївна", "Сергіївна", "Олексіївна", "Анатоліївна"]
        def malePatronymics = ["Іванович", "Петрович", "Олександрович", "Васильович", "Миколайович", "Сергійович", "Олексійович", "Анатолійович"]

        String firstName = isMale ? faker.name().maleFirstName() : faker.name().femaleFirstName()
        String lastName = faker.name().lastName()
        String secondName = isMale
                ? malePatronymics[faker.random().nextInt(malePatronymics.size())]
                : femalePatronymics[faker.random().nextInt(femalePatronymics.size())]
        String birthday = faker.timeAndDate().birthday(18, 60, "yyyy-MM-dd")
        return new Person(firstName, lastName, secondName, birthday)
    }

    static void createTarGz(Path source, Path output) {
        try (
                OutputStream fos = Files.newOutputStream(output)
                BufferedOutputStream bos = new BufferedOutputStream(fos)
                GzipCompressorOutputStream gzos = new GzipCompressorOutputStream(bos)
                TarArchiveOutputStream taos = new TarArchiveOutputStream(gzos)
        ) {
            taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX)
            addFilesToTarGz(source, "", taos)
        }
    }

    private static void addFilesToTarGz(Path path, String parent, TarArchiveOutputStream taos) {
        String entryName = parent + path.getFileName().toString()
        TarArchiveEntry entry = new TarArchiveEntry(path.toFile(), entryName)
        taos.putArchiveEntry(entry)

        if (Files.isRegularFile(path)) {
            try (InputStream fis = Files.newInputStream(path)) {
                fis.transferTo(taos)
            }
            taos.closeArchiveEntry()
        } else {
            taos.closeArchiveEntry()
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                for (Path child : stream) {
                    addFilesToTarGz(child, entryName + "/", taos)
                }
            }
        }
    }
}
