import com.xlson.groovycsv.PropertyMapper
import groovy.io.FileType

import static com.xlson.groovycsv.CsvParser.parseCsv

//TODO: This needs to be fixed-pvsubrah.
File dataDirectory = new File("/Users/pvsubrah/Development/kuali/ole-1.5/ole-app/ole-db/ole-liquibase/ole-liquibase-changeset/src/main/resources/");
println "Scanning Dir: $dataDirectory"

def sourceFiscalYear = "2014"
def destFiscalYear = "2015"

def deepcopy(orig) {
    bos = new ByteArrayOutputStream()
    oos = new ObjectOutputStream(bos)
    oos.writeObject(orig); oos.flush()
    bin = new ByteArrayInputStream(bos.toByteArray())
    ois = new ObjectInputStream(bin)
    return ois.readObject()
}


def closure = {
    File file ->
        println file
        def PropertyMapper propertyMapper
        def Map map
        def List modifiedList = new ArrayList();
        def String[] newValues;
        def String[] values;
        def data = parseCsv(new FileReader(file));

        for (line in data) {
            propertyMapper = (PropertyMapper) line;
            map = (Map) (propertyMapper.columns);
            if (map.containsKey("UNIV_FISCAL_YR")) {
                values = (String[]) propertyMapper.values
                if (values[map.get("UNIV_FISCAL_YR")] == sourceFiscalYear) {
                    newValues = deepcopy(values);
                    newValues[map.get("UNIV_FISCAL_YR")] = destFiscalYear;
                    try {
                        newValues[map.get("OBJ_ID")] = UUID.randomUUID() as String;
                    } catch (Exception e) {
                        //This means that there wasn't an obj_id in that particular csv, which is fine.
                    }

                    def newList = Arrays.asList(newValues)
                    modifiedList.addAll(newList);
                }
            }
        }
        if (!modifiedList.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            int count = 1;
            for (java.util.Iterator iterator1 = modifiedList.iterator(); iterator1.hasNext();) {
                String next = iterator1.next();
                stringBuilder.append("\"").append(next).append("\"");
                if (count < map.size()) {
                    stringBuilder.append(",");
                } else {
                    stringBuilder.append("\n")
                    count = 0;
                }
                count++;
            }
            file.append(stringBuilder);
        }
}

dataDirectory.eachFileRecurse FileType.FILES, closure

