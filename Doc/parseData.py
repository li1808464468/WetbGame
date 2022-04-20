# -*- coding: utf-8 -*- 
import xdrlib, sys, os
import xlrd

global fileName, keyRow
fileName = 'data_JewelBlast.xlsx'

keyRow = 2#key行
typeRow = 1#数据类型行


def parse_excel(fileName):
	data = xlrd.open_workbook(fileName)
	sheetsNames = data.sheet_names()

	for sheetName in sheetsNames:
		table = data.sheet_by_name(sheetName)

		# print table.nrows, table.ncols#行数和列数
		keys = table.row(keyRow)
		contentStr = '{\n\t\"data\":'

		tableType = table.cell(1, 0).value
		if tableType == 'key':
			contentStr += '{\n'
		elif tableType == 'index':
			contentStr += '[\n'

		for row in xrange(keyRow + 1, table.nrows):
			if tableType == 'key':
				contentStr += '\t\t\"' + str(table.cell(row, 0).value) + '\" : '
			else:
				contentStr += '\t\t'
			contentStr += '{'

			for col in xrange(0, table.ncols):
				contentStr += "\"" +  keys[col].value + '\" : '

				dataType = table.cell(typeRow, col).value
				if dataType == 'n' or dataType == 'index':
					contentStr += str(int(table.cell(row, col).value))
				elif dataType == 's' or dataType == 'key':
					if (sheetName == "data_level_1") and keys[col].value == "level_reward":
						contentStr += '\"' + str(table.cell(row, col).value).encode('utf-8').replace(".0", "") + '\"'
					else:
						contentStr += '\"' + str(table.cell(row, col).value).encode('utf-8') + '\"'
				elif dataType == 'f':
					contentStr += str(float(table.cell(row, col).value))

				if col < table.ncols - 1:
					contentStr += ', '

			contentStr += '}'
			if (row < table.nrows - 1):
				contentStr += ','
			contentStr += '\n'


		if tableType == 'key':
			contentStr += '}\n'
		elif tableType == 'index':
			contentStr += '\t]\n'

		contentStr += '}\n'


		# print contentStr

		# if not os.path.exists('out'):
		# 	os.mkdir('out')
		fp = open('../Assets/Resources/GameConfig/' + sheetName + ".json", 'w')
		fp.write(contentStr)
		print sheetName + ' successful!'

def main():
   	parse_excel(fileName)
   		

if __name__=="__main__":
    main()