Fix malformed XML declaration in strings.xml causing resource merge failure.

This PR fixes the XML declaration in app/src/main/res/values/strings.xml by adding required whitespace and ensuring UTF-8 encoding without BOM.

- Corrected first line to: <?xml version="1.0" encoding="utf-8"?>
- Confirmed file saved without BOM

This should resolve the SAXParseException during :app:mergeReleaseResources in CI.
