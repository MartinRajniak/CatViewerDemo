import Combine
import shared

class CatsStore: ObservableObject {
    private let viewModel = CatsViewModel()
    
    @Published var cats: [Cat] = []
    @Published var hasNextPage: Bool = false
    
    @Published var categories: [CategoryModel] = []
    @Published var mimeTypes: [MimeTypeModel] = []
    
    func start() {
        viewModel.cats.watch { cats in
            guard let list = cats?.compactMap({ $0 as? Cat}) else {
                return
            }
            self.cats = list
            self.hasNextPage = true // FIXME: should check if there is more data
        }
        
        viewModel.categories.watch { categories in
            guard let list = categories?.compactMap({ $0 as? CategoryModel}) else {
                return
            }
            self.categories = list
        }

        viewModel.mimeTypes.watch { categories in
            guard let list = categories?.compactMap({ $0 as? MimeTypeModel}) else {
                return
            }
            self.mimeTypes = list
        }
    }
    
    func onScrolledToTheEnd() {
        viewModel.onScrolledToTheEnd()
    }
    
    func onCategoryChecked(categoryId: Int32, checked: Bool) {
        viewModel.onCategoryChecked(categoryId: categoryId, checked: checked)
    }
    
    func onMimeTypeChecked(mimeTypeId: Int32, checked: Bool) {
        viewModel.onMimeTypeChecked(mimeTypeId: mimeTypeId, checked: checked)
    }
}
