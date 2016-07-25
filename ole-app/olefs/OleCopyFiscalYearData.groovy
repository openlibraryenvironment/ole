import groovy.io.FileType
import groovy.xml.XmlUtil



//TODO: Refactor this to pick from the relative project root path. Will do it for the 1.0.0-M2 release.
File dataDirectory = new File( "/home/likewise-open/HTCINDIA/sureshss/workspace/OLE-3.0_New/ole/ole-app/ole-db/ole-impex/ole-impex-master/src/main/resources" )
println "Scanning Dir: $dataDirectory"

def sourceFiscalYear = 2016
def destFiscalYear = 2017

dataDirectory.eachFile( FileType.FILES ) { file ->
    if ( file.text.contains( """UNIV_FISCAL_YR="$sourceFiscalYear.0" """ )
            && !file.text.contains( """UNIV_FISCAL_YR="$destFiscalYear.0" """ ) ) {
        println file
        def fileXml = new XmlParser().parse( file )
        println "Matching Records:"

        fileXml.children().findAll { rec -> rec.@UNIV_FISCAL_YR == "$sourceFiscalYear.0" }.each { rec ->
            println XmlUtil.serialize( rec )
            def clonedNode = new XmlParser().parseText( XmlUtil.serialize( rec ) )
            if(null != rec.@UNIV_DT){
                clonedNode.@UNIV_DT = (Integer.parseInt(rec.@UNIV_DT.substring(0,4))+1).toString()+rec.@UNIV_DT.substring(4,rec.@UNIV_DT.length())
            }
            clonedNode.@UNIV_FISCAL_YR = "$destFiscalYear.0"
            if ( clonedNode.attributes().containsKey( "OBJ_ID" ) ) {
                clonedNode.@OBJ_ID = UUID.randomUUID() as String
            }
            if ( clonedNode.attributes().containsKey( "VER_NBR" ) ) {
                clonedNode.@VER_NBR = "1.0"
            }
            fileXml.append( clonedNode )
        }

        file.write(XmlUtil.serialize(fileXml))
    }
}