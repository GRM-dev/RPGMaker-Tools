
namespace Rpg
{
	/// <summary>
	/// Flags for indicating current state of parsing an IL file
	/// </summary>
	internal enum ParserState
	{
		Normal,
		ClassDeclaration,
		Class,
		DeleteExportDependency,
		MethodDeclaration,
		MethodProperties,
		Method,
		DeleteExportAttribute
	}
}
