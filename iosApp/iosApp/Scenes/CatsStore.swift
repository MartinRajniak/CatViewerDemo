import Combine
import shared

class CatsStore: ObservableObject {
    private let viewModel = CatsViewModel()
    
    @Published var cats: [Cat] = []
    
    func fetch() {
        viewModel.cats.watch { cats in
            guard let list = cats?.compactMap({ $0 as? Cat}) else {
                return
            }
            self.cats = list
        }
    }
}
