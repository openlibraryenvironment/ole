/**
 * Generic interface class for running multiple XSL transforms on XML
 * documents.
 * @param stylesheet either a loaded XML document (ECMA standard) or the URL
 * to a stylesheet (IE)
 * @constructor
 */


function XSLTransformer(stylesheet) {
    this.stylesheet = stylesheet;
    this.loaded = false;
    this.msxml = false;
}


// customizes the XSLTransformer class for IE or standards-compliant
// browsers.
(function() {

    if ( window.ActiveXObject ) { // IE
        // we need a different "load" phase for IE, since it needs to use all sorts
        // of crazy incantations, and only sorta works in the end.
        XSLTransformer.prototype.ensureLoad = function () {
            if ( !this.loaded ) {
                var xslt = new ActiveXObject("Msxml2.XSLTemplate");
                var xslDoc = new ActiveXObject("Msxml2.FreeThreadedDOMDocument");
                xslDoc.async = false;
                xslDoc.load(this.stylesheet);
                xslt.stylesheet = xslDoc;
                var xslProc = xslt.createProcessor();
                this.processor = xslProc;
                this.loaded = true;
            }
        }

        XSLTransformer.prototype.transform = function(xml) {
            this.ensureLoad();
            this.processor.reset();
            this.processor.input = xml;
            this.processor.transform();
            return this.processor.output;
        }
    } else { // ECMAScript
        XSLTransformer.prototype.processor = new XSLTProcessor();
        XSLTransformer.prototype.serializer = new XMLSerializer();
        XSLTransformer.prototype.ensureLoad= function() {
            if ( !this.loaded ) {
                this.processor.importStylesheet(this.stylesheet);
                this.loaded = true;
            }
        }

        XSLTransformer.prototype.transform = function(xml) {
            this.ensureLoad();
            var result = this.processor.transformToDocument(xml);
            return this.serializer.serializeToString(result);
        }
    }
})();


