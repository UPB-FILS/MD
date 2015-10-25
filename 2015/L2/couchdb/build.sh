#!/bin/bash
rm *.zip; zip -r todo.zip  todo manifest.json -x "*/\.DS_Store*"